package com.wiki.config;

import com.wiki.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Web MVC 配置类
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Value("${kurowiki.upload.path}")
    private String uploadPath;

    @Value("${kurowiki.upload.url-prefix}")
    private String urlPrefix;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**")

                // 放行：用户公开接口
                .excludePathPatterns("/api/user/login")
                .excludePathPatterns("/api/user/register")
                .excludePathPatterns("/api/user/logout")
                .excludePathPatterns("/api/user/info")

                // 放行：Knife4j API 文档
                .excludePathPatterns("/doc.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs/**");
    }

    /**
     * 虚拟路径映射：将 /images/** 映射到本地磁盘目录
     * 访问 http://localhost:8088/images/xxx.png 即可获取上传的图片
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(urlPrefix + "**")
                .addResourceLocations("file:" + uploadPath);
    }

    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
