package com.itnmz.community.model.dto;

import com.itnmz.community.utils.RegexUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 接收前端参数
 * 注册入参。
 *
 * 手机号、验证码、密码必填；昵称选填，不填时由服务层生成默认昵称。
 */
@Data
public class RegisterDTO {

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = RegexUtils.PHONE_REGEX, message = "手机号格式不正确")
    private String phone;

    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "验证码必须是 6 位数字")
    private String code;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度需为 6~20 位")
    private String password;

    /** 选填，为空时自动生成「用户 + 手机号后四位」 */
    @Size(max = 20, message = "昵称最多 20 个字符")
    private String nickname;
}
