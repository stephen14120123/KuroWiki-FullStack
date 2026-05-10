package com.wiki.aspect;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 接口日志与耗时监控切面
 * 拦截所有 Controller 方法，记录请求信息和执行耗时。
 * 超过 500ms 的接口标记为慢接口并打印警告。
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    /** 慢接口阈值（毫秒） */
    private static final long SLOW_THRESHOLD_MS = 500;

    @Around("execution(* com.wiki.controller.*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = "UNKNOWN";
        String uri = "UNKNOWN";
        String user = "anonymous";

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
            uri = request.getRequestURI();
            // 从拦截器设置的属性中获取用户名
            Object username = request.getAttribute("username");
            if (username != null) {
                user = username.toString();
            }
        }

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.info("[API] {} {} -> {}.{}() | user={}", method, uri, className, methodName, user);

        // 执行目标方法
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            long costTime = System.currentTimeMillis() - startTime;
            log.error("[API ERROR] {}.{}() | cost={}ms | error={}", className, methodName, costTime, e.getMessage());
            throw e;
        }

        // 计算耗时
        long costTime = System.currentTimeMillis() - startTime;

        if (costTime > SLOW_THRESHOLD_MS) {
            // 慢接口警告
            String params = truncateParams(joinPoint.getArgs());
            log.warn("[WARN] Slow API detected! {}.{}() | cost={}ms | user={} | params={}",
                    className, methodName, costTime, user, params);
        } else {
            log.info("[API] {}.{}() completed | cost={}ms", className, methodName, costTime);
        }

        return result;
    }

    /**
     * 截断参数字符串，避免日志过长
     */
    private String truncateParams(Object[] args) {
        if (args == null || args.length == 0) return "[]";
        try {
            String json = JSON.toJSONString(args);
            return json.length() > 500 ? json.substring(0, 500) + "..." : json;
        } catch (Exception e) {
            return Arrays.toString(args);
        }
    }
}
