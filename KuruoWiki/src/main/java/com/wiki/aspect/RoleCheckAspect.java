package com.wiki.aspect;

import com.wiki.common.RequiresRole;
import com.wiki.common.Result;
import com.wiki.utils.JwtUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 角色权限验证切面
 * <p>
 * 切入点：所有标记了 @RequiresRole 的方法。
 * 逻辑：从请求头中提取 JWT Token，解析出用户角色，
 * 与注解要求的角色等级比对，不满足则返回 403。
 * </p>
 */
@Aspect
@Component
public class RoleCheckAspect {

    private static final String BEARER_PREFIX = "Bearer ";

    @Around("@annotation(requiresRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint, RequiresRole requiresRole) throws Throwable {
        // 1. 获取当前请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return Result.error("无法获取请求上下文");
        }

        HttpServletRequest request = attributes.getRequest();

        // 2. 优先从 Request Attribute 获取角色（拦截器已解析的情况）
        Integer userRole = (Integer) request.getAttribute("userRole");

        // 3. 如果拦截器未解析（路径被放行），则自行从 Token 中提取
        if (userRole == null) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
                return Result.error("未提供身份凭证，请先登录");
            }

            String token = authHeader.substring(BEARER_PREFIX.length());

            // 校验 Token 有效性
            if (JwtUtil.isTokenExpired(token)) {
                return Result.error("身份凭证已过期，请重新登录");
            }

            // 提取角色
            userRole = JwtUtil.getUserRole(token);
            if (userRole == null) {
                return Result.error("无法识别用户身份，请重新登录");
            }
        }

        // 4. 角色等级比对：用户角色值必须 >= 注解要求的值
        if (userRole < requiresRole.value()) {
            return Result.error("权限不足：该操作需要管理员权限");
        }

        // 5. 权限校验通过，执行目标方法
        return joinPoint.proceed();
    }
}
