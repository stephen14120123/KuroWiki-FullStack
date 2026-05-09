package com.wiki.config;

import com.wiki.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置类
 * <p>
 * 注册登录拦截器，配置拦截与放行规则。
 * 原则：默认拦截需要鉴权的接口，显式放行公开接口和静态资源。
 * </p>
 *
 * @author KuroWiki
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                // ===== 需要登录才能访问的接口 =====
                .addPathPatterns("/api/strategies/**")   // 攻略发布/编辑/删除
                .addPathPatterns("/api/admin/**")        // 管理员后台
                .addPathPatterns("/api/user/myStrategies") // 我的攻略

                // ===== 放行：登录与注册接口 =====
                .excludePathPatterns("/api/user/login")
                .excludePathPatterns("/api/user/logout")
                .excludePathPatterns("/api/user/info")

                // ===== 放行：公开数据查询接口 =====
                .excludePathPatterns("/api/characters")
                .excludePathPatterns("/api/characters/**")
                .excludePathPatterns("/api/weapons")
                .excludePathPatterns("/api/weapons/**")
                .excludePathPatterns("/api/echoes")
                .excludePathPatterns("/api/echoes/**")
                .excludePathPatterns("/api/weapon/list")
                .excludePathPatterns("/api/echo/list")

                // ===== 放行：静态资源 =====
                .excludePathPatterns("/", "/*.html", "/css/**", "/js/**", "/images/**", "/favicon.ico");
    }

    /**
     * 跨域配置（支持 Vue 前端开发环境访问）
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
