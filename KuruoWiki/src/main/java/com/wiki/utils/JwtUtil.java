package com.wiki.utils;

import com.wiki.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类
 * <p>
 * 基于 jjwt 实现 Token 的生成、解析与校验。
 * 使用 HMAC-SHA256 签名算法，密钥长度满足 256 位安全要求。
 * </p>
 *
 * @author KuroWiki
 */
public final class JwtUtil {

    /** 签名密钥（至少 32 字节以满足 HS256 安全要求） */
    private static final String SECRET = "KuroWiki_Super_Secret_Key_2024_Enterprise_Edition";

    /** Token 有效期：7 天 */
    private static final long EXPIRATION_MS = 7 * 24 * 60 * 60 * 1000L;

    /** 签发者标识 */
    private static final String ISSUER = "KuroWiki";

    /** 生成 SecretKey 实例（线程安全，只初始化一次） */
    private static final SecretKey SIGNING_KEY =
            Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    private JwtUtil() {
        // 工具类禁止实例化
    }

    /**
     * 生成 JWT Token
     *
     * @param user 当前登录用户实体
     * @return 签名后的 JWT 字符串
     */
    public static String generateToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject(String.valueOf(user.getId()))
                .claim("userId", user.getId())
                .claim("username", user.getUsername())
                .claim("nickname", user.getNickname())
                .claim("role", user.getRole())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析 Token，返回 Claims 载荷
     * <p>
     * 如果 Token 无效、被篡改或已过期，返回 null。
     * </p>
     *
     * @param token JWT 字符串（不含 "Bearer " 前缀）
     * @return Claims 对象，解析失败返回 null
     */
    public static Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 校验 Token 是否已过期
     *
     * @param token JWT 字符串
     * @return true 表示已过期或无效，false 表示仍在有效期内
     */
    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 从 Token 中提取用户 ID
     *
     * @param token JWT 字符串
     * @return 用户 ID，解析失败返回 null
     */
    public static Integer getUserId(String token) {
        Claims claims = parseToken(token);
        return claims != null ? claims.get("userId", Integer.class) : null;
    }

    /**
     * 从 Token 中提取用户角色
     *
     * @param token JWT 字符串
     * @return 角色值（0-普通用户, 1-管理员），解析失败返回 null
     */
    public static Integer getUserRole(String token) {
        Claims claims = parseToken(token);
        return claims != null ? claims.get("role", Integer.class) : null;
    }
}
