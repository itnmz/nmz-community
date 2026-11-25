package com.itnmz.community.common.exception;

/**
 * 业务异常（BusinessException）
 *
 * 用于表达「请求本身合法，但业务规则不允许」的情况，例如验证码错误、手机号已注册、密码错误。
 * 继承 RuntimeException 而不是 Exception：
 *  - 不需要在每个方法签名上写 throws，调用链不会被污染；
 *  - Spring 的 @Transactional 默认只对 RuntimeException 回滚。
 */
//IDEA 识别到该类（异常类）直接或间接继承了 Throwable / Exception
public class BusinessException extends RuntimeException {

    /** 业务状态码，默认 400（客户端可预期的错误） */
    private final Integer code;

    public BusinessException(String message) {
        this(400, message);
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
