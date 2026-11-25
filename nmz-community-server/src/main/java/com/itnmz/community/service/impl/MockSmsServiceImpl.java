package com.itnmz.community.service.impl;

import com.itnmz.community.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 本地开发用的短信实现：不真正发短信，只把验证码打印到控制台。
 *
 * matchIfMissing = true 表示「配置里没写 sms.mock 时也默认启用这个实现」，
 * 保证第一次启动就能跑通，不会因为漏配配置而启动失败。
 *
 * 注意：目前没有第二个实现类，所以 sms.mock 只能保持 true；
 * 改成 false 会导致 SmsService 没有可用 Bean，应用启动失败。
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "sms.mock", havingValue = "true", matchIfMissing = true)
public class MockSmsServiceImpl implements SmsService {

    @Override
    public void sendVerificationCode(String phone, String code) {
        log.warn("======================================================");
        log.warn("【本地 MOCK 短信】");
        log.warn("  手机号 : {}", phone);
        log.warn("  验证码 : >>> {} <<<   （5 分钟内有效）", code);
        log.warn("======================================================");
    }
}
