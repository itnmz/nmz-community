package com.itnmz.community.model.dto;

import com.itnmz.community.utils.RegexUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

//Validation：验证
/**
 * 接收前端参数
 * 获取短信验证码的入参。
 */
@Data
public class SendCodeDTO {

    //@NotBlank注解：是否包含非空有效字符（非 null、非 ""、非纯空格）
    @NotBlank(message = "手机号不能为空")
    //校验字符串是否匹配指定正则表达式，属于参数校验注解（配合 `@Valid` / `@Validated` 生效）
    //null 默认校验通过
    @Pattern(regexp = RegexUtils.PHONE_REGEX, message = "手机号格式不正确")
    private String phone;
}
