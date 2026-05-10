package com.wiki.controller;

import com.wiki.common.Result;
import com.wiki.entity.User;
import com.wiki.service.UserService;
import com.wiki.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户模块接口
 * <p>
 * 提供登录、获取用户信息、登出等功能。
 * 登录成功后签发 JWT Token，前端后续请求需在 Authorization 头中携带。
 * </p>
 *
 * @author KuroWiki
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录接口
     * <p>
     * 接收用户名和密码，校验成功后返回 JWT Token 和用户基本信息。
     * </p>
     *
     * @param username 用户名
     * @param password 密码
     * @return 包含 token 和 user 信息的响应
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String username = params != null ? params.get("username") : null;
        String password = params != null ? params.get("password") : null;

        // 参数校验
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return Result.error("用户名和密码不能为空");
        }

        // 调用 Service 层校验用户名密码
        User user = userService.login(username.trim(), password);
        if (user == null) {
            return Result.error("终端验证拒绝：用户名或密码错误");
        }

        // 签发 JWT Token
        String token = JwtUtil.generateToken(user);

        // 组装响应数据（不返回密码）
        user.setPassword(null);
        Map<String, Object> data = new HashMap<>(4);
        data.put("token", token);
        data.put("user", user);

        return Result.success(data);
    }

    /**
     * 获取当前登录用户信息
     * <p>
     * 从 Authorization 请求头中解析 Token，提取用户信息返回。
     * 该接口不经过拦截器保护（已在 WebConfig 中排除），但需要有效 Token。
     * </p>
     *
     * @param request HTTP 请求对象
     * @return 用户信息
     */
    @GetMapping("/info")
    public Result<User> getUserInfo(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.error("未提供有效的身份凭证");
        }

        String token = authHeader.substring(7);

        // 校验 Token 是否过期
        if (JwtUtil.isTokenExpired(token)) {
            return Result.error("身份凭证已过期，请重新登录");
        }

        // 解析 Token 载荷
        Claims claims = JwtUtil.parseToken(token);
        if (claims == null) {
            return Result.error("身份凭证无效");
        }

        // 从 Token 中提取用户信息（无需查库，减少 DB 压力）
        User user = new User();
        user.setId(claims.get("userId", Integer.class));
        user.setUsername(claims.get("username", String.class));
        user.setNickname(claims.get("nickname", String.class));
        user.setRole(claims.get("role", Integer.class));

        return Result.success(user);
    }

    /**
     * 用户注册接口
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody Map<String, String> params) {
        String username = params != null ? params.get("username") : null;
        String password = params != null ? params.get("password") : null;
        String nickname = params != null ? params.get("nickname") : null;

        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return Result.error("用户名和密码不能为空");
        }
        if (password.length() < 6) {
            return Result.error("密码长度不能少于6位");
        }

        userService.register(username.trim(), password, nickname);
        return Result.success("注册成功");
    }

    /**
     * 用户登出
     * <p>
     * JWT 是无状态的，服务端不维护会话。
     * 登出由前端清除本地存储的 Token 实现。
     * 此接口仅作为语义化端点存在。
     * </p>
     *
     * @return 登出成功提示
     */
    @GetMapping("/logout")
    public Result<String> logout() {
        return Result.success("终端连接已断开");
    }

    /**
     * 获取当前用户发布的攻略列表
     *
     * @param request HTTP 请求对象
     * @return 用户攻略列表
     */
    @GetMapping("/myStrategies")
    public Result<?> getMyStrategies(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.error("请先登录");
        }

        String token = authHeader.substring(7);
        Integer userId = JwtUtil.getUserId(token);
        if (userId == null) {
            return Result.error("身份凭证无效");
        }

        return Result.success(userService.getUserStrategies(userId));
    }
}
