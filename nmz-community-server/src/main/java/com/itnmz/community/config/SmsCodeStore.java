package com.itnmz.community.config;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 短信验证码的存取封装。
 *
 * key 形如 sms:code:13800138000，value 是 6 位数字，靠 Redis 的 TTL 自动过期。
 * 用 StringRedisTemplate 而不是 RedisTemplate：
 * 前者用 String 序列化，redis-cli 里能直接看懂；后者默认 JDK 序列化，存进去是乱码。
 *
 * 说明：放在 config 包是按当前约定，语义上它更像 auth/sms 下的组件，
 * 以后随认证模块一起搬走即可，不影响任何调用方（只依赖类型，不依赖包名）。
 */
@Component
public class SmsCodeStore {

    private static final String KEY_PREFIX = "sms:code:";

    private final StringRedisTemplate redisTemplate;

    public SmsCodeStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    /**
     * 1、保存验证码并设置过期时间，SET key value EX ttl 一条命令完成。
     */
    public void save(String phone, String code, Duration ttl) {
        redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, ttl);
    }

    /**
     * 2、读取验证码。不存在或已过期都返回 null。
     */
    public String get(String phone) {
        return redisTemplate.opsForValue().get(KEY_PREFIX + phone);
    }

    /**
     * 3、删除验证码，用于「一次性消费」：校验通过后立刻删除，防止同一个码被重复使用。
     */
    public boolean delete(String phone) {
        return Boolean.TRUE.equals(redisTemplate.delete(KEY_PREFIX + phone));
    }
}
