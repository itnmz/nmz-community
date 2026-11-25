package com.itnmz.community.service;

/**
 * 短信发送能力。
 *
 * 业务层只依赖这个接口，具体是「打印到控制台」还是「调阿里云」由实现类决定，
 * 以后接入真实短信服务时，只需新增一个实现类并切换配置，业务代码一行不改。
 */
public interface SmsService {

    /**
     * 发送验证码短信。
     *
     * @param phone 目标手机号
     * @param code  已经生成好的验证码
     */
    void sendVerificationCode(String phone, String code);
}
