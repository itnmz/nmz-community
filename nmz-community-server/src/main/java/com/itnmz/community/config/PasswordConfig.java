package com.itnmz.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码编码器配置。
 *
 * 只用 BCrypt 做加密和校验，没有引入 spring-boot-starter-security，
 * 所以不会产生默认的登录拦截链，接口不会被无端锁住。
 */
@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 默认强度 10，单次加密约 50~100ms，足以对抗离线爆破又不至于拖慢登录
        return new BCryptPasswordEncoder();
    }
}
