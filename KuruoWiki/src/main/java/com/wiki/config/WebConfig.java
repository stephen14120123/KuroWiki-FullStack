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
                // ===== 拦截所有 /api/** 接口 =====
                .addPathPatterns("/api/**")

                // ===== 放行：登录与用户公开接口 =====
                .excludePathPatterns("/api/user/login")
                .excludePathPatterns("/api/user/logout")
                .excludePathPatterns("/api/user/info")

                // ===== 放行：Knife4j API 文档 =====
                .excludePathPatterns("/doc.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs/**");
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
