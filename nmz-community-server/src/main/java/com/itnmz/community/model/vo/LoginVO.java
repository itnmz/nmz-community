package com.itnmz.community.model.vo;

import lombok.Data;

/**
 * 注册成功/登录 后的返回体。
 *
 * token 由前端保存（localStorage），之后每个请求带在 Authorization: Bearer <token> 里。
 */
@Data
public class LoginVO {

    //登录token
    private String token;

    //用户信息
    private UserVO userInfo;
}
