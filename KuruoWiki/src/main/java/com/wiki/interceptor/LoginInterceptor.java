package com.wiki.interceptor;

import com.wiki.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录鉴权拦截器
 * <p>
 * 实现 HandlerInterceptor 接口，在请求到达 Controller 之前进行 JWT 校验。
 * 校验通过后将用户信息存入 Request Attribute，供后续业务层使用。
 * </p>
 *
 * @author KuroWiki
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    /** Authorization 请求头前缀 */
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 放行 CORS 预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 2. GET 请求到 /api/strategies 不需要登录（公开查看攻略）
        if ("GET".equalsIgnoreCase(request.getMethod())
                && request.getRequestURI().startsWith("/api/strategies")) {
            return true;
        }

        // 3. 从请求头中提取 Token
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String token = authHeader.substring(BEARER_PREFIX.length());

            // 4. 校验 Token 是否过期
            if (JwtUtil.isTokenExpired(token)) {
                sendUnauthorized(response, "Token 已过期，请重新登录");
                return false;
            }

            // 5. 解析 Token 载荷
            Claims claims = JwtUtil.parseToken(token);
            if (claims != null) {
                // 6. 将用户信息存入 Request，供 Controller/Service 使用
                request.setAttribute("currentUserId", claims.get("userId", Integer.class));
                request.setAttribute("userRole", claims.get("role", Integer.class));
                request.setAttribute("username", claims.get("username", String.class));
                return true;
            }
        }

        // 7. 未携带 Token 或 Token 无效，返回 401
        sendUnauthorized(response, "终端验证失败，请登录后操作");
        return false;
    }

    /**
     * 向客户端返回 401 未授权响应
     *
     * @param response HTTP 响应对象
     * @param message  错误提示信息
     */
    private void sendUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(
                String.format("{\"code\": 401, \"message\": \"%s\", \"data\": null}", message)
        );
    }
}
