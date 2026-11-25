package com.itnmz.community.model.vo;

import com.itnmz.community.model.entity.User;
import lombok.Data;

/**
 * 对外暴露的用户信息。
 *
 * 存在的唯一理由：绝不能把 User 实体直接返回前端——里面有 password 字段，
 * 序列化出去就是把密码哈希白送。VO 只挑能公开的字段。
 */
@Data
public class UserVO {

    private Long id;
    private String phone; //手机号
    private String nickname; //昵称
    private String avatar; //用户头像地址（avatar：头像）
    private String role; //用户标识（user、admin）

    public static UserVO from(User user) {
        if (user == null) {
            return null;
        }
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setPhone(user.getPhone());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setRole(user.getRole());
        return vo;
    }
}
