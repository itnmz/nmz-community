package com.itnmz.community.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 签发与解析。
 *
 * 密钥来自配置 nmz.jwt.secret，长度不足 32 字节会在启动时直接抛异常，
 * 而不是等到登录时才莫名其妙地把密钥截断——这是 jjwt 最坑的地方之一。
 */
@Component
public class JwtUtil {

    /** 解析后放在 Claims(翻译叫「声明」，本质就是一个 Map) 里的用户名字段名 */
    public static final String CLAIM_PHONE = "phone";

    private final SecretKey key;
    private final long expireMillis;

    public JwtUtil(@Value("${nmz.jwt.secret}") String secret,
                   @Value("${nmz.jwt.expire-minutes:1440}") long expireMinutes) {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException(
                    "nmz.jwt.secret 至少需要 32 字节（HMAC-SHA256 要求），当前只有 " + bytes.length + " 字节");
        }
        //生成密钥对象
        this.key = Keys.hmacShaKeyFor(bytes);
        //换算成毫秒
        this.expireMillis = expireMinutes * 60_000L;
    }

    /**
     * 签发 token。主体放 userId，昵称等可变信息不要放进来。
     */
    public String generateToken(Long userId, String phone) {
        // 1. 获取当前系统时间，作为签发时间和计算过期时间的基准
        Date now = new Date();

        return Jwts.builder()
                // 2. 设置标准声明 `sub` (Subject)：存放令牌主体（通常是全局唯一的用户 ID）
                .subject(String.valueOf(userId))

                // 3. 添加自定义私有声明 (Claim)：将手机号作为附加信息存入载荷（Payload）
                .claim(CLAIM_PHONE, phone)

                // 4. 设置标准声明 `iat` (Issued At)：记录 Token 的签发时间
                .issuedAt(now)

                // 5. 设置标准声明 `exp` (Expiration)：当前时间 + 有效毫秒数 = 绝对过期时间点
                .expiration(new Date(now.getTime() + expireMillis)) //到期时间

                // 6. 数字签名：使用后端保密的密钥 `key` 对头部和载荷进行哈希签名（防篡改）
                .signWith(key)
                // 7. 压缩生成：按 JWT 规范进行 Base64URL 编码并拼接成最终字符串
                .compact();
    }

    /**
     * 解析 token。签名不对或已过期会抛 JwtException，由调用方决定怎么处理。
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 取用户 ID，这是后面拦截器唯一需要的东西。
     */
    public Long getUserId(String token) {
        return Long.valueOf(parseToken(token).getSubject());
    }

    /**
     * 从 Authorization 头里剥掉 "Bearer " 前缀。
     */
    public String resolveToken(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            return null;
        }
        if (authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7).trim();
        }
        return authorizationHeader.trim();
    }
}
