package com.itnmz.community.controller;

import com.itnmz.community.common.result.Result;
import com.itnmz.community.model.dto.LoginDTO;
import com.itnmz.community.model.dto.RegisterDTO;
import com.itnmz.community.model.dto.SendCodeDTO;
import com.itnmz.community.model.vo.LoginVO;
import com.itnmz.community.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证接口。
 *
 * 路径统一挂在 /nmz/v1/auth 下，重建登录拦截器时把这个前缀整体加入白名单即可。
 */
@RestController  //
@RequestMapping("/nmz/v1/auth")
@RequiredArgsConstructor  //通过构造器注入依赖
//@Validated --> 关键：必须加在类上，散装参数的校验才会生效
public class AuthController {

    private final AuthService authService;


    /**
     * 1、获取短信验证码。本地开发时验证码只打印在后端控制台，响应体里绝不返回。
     */
    @PostMapping("/sms-code")
    public Result<Void> sendCode(@RequestBody @Valid SendCodeDTO dto) {
        authService.sendCode(dto.getPhone());
        return Result.ok();
    }


    /**
     * 2、注册(register)：手机号 + 验证码 + 密码（昵称选填），成功后直接返回 token，省去再登一次。
     */
    @PostMapping("/register")
    public Result<LoginVO> register(@RequestBody @Valid RegisterDTO dto) {
        return Result.ok(authService.register(dto));
    }


    /**
     * 3、登录：手机号 + 密码。
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody @Valid LoginDTO dto) {
        return Result.ok(authService.login(dto));
    }
}
