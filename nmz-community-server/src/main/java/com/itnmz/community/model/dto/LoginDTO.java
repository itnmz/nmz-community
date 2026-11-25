package com.itnmz.community.model.dto;

import com.itnmz.community.utils.RegexUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 接收前端参数
 * 登录入参：手机号 + 密码。
 *
 * 密码这里只校验非空，不校验长度规则——
 * 登录失败时不应该通过报错信息泄露「密码策略」这类信息。
 */
@Data
public class LoginDTO {

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = RegexUtils.PHONE_REGEX, message = "手机号格式不正确")
    private String phone;

    @NotBlank(message = "密码不能为空")
    private String password;
}
