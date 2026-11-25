package com.itnmz.community.config;

import com.itnmz.community.auth.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置。
 *
 * ① CORS：前端 5173 调 8080 必需，不配浏览器直接拦截，表现为「Postman 能通、浏览器报跨域」。
 * ② 登录拦截器：默认拦截所有请求，只放行白名单，新增接口默认受保护。
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;

    public WebMvcConfig(LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(  //排除的路径
                        // 登录、注册、发验证码不能要求登录
                        "/nmz/v1/auth/**",
                        // 健康检查
                        "/api/health/**",
                        // Swagger 相关，否则文档页面自己都打不开
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        // Spring Boot 错误转发路径
                        "/error",
                        "/favicon.ico"
                );
    }


    /**
     * 作用是告诉浏览器：
     * 允许指定的前端地址跨域调用后端的哪些接口、使用哪些请求方式、携带哪些请求头和凭证，
     * 从而解决前后端分离开发时的浏览器同源策略拦截报错。
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 开发期放行本机任意端口：Vite 在 5173 被占用时会自动跳到 5174，
                // 写死端口会导致「前端明明起了却一直跨域失败」。生产环境要收紧成具体域名。
                .allowedOriginPatterns("http://localhost:*", "http://127.0.0.1:*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                // 允许前端带 Authorization 头
                .allowCredentials(true)
                // 预检结果缓存 1 小时，减少 OPTIONS 往返
                .maxAge(3600);
    }
}
