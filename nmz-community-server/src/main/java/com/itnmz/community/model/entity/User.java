package com.itnmz.community.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 用户表
 * @TableName nmz_user
 */
@TableName(value ="nmz_user")
@Data
@Builder
//无参构造方法(Jackson 反序列化 JSON（接收前端 @RequestBody）或 MyBatis-Plus 反射实例化对象时报错)
@NoArgsConstructor
//@Builder 依赖全参构造
@AllArgsConstructor
public class User {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 账号/手机号
     */
    private String phone;

    /**
     * BCrypt加密密码
     * @JsonIgnore：序列化和反序列化时忽略该字段
     */
    @JsonIgnore
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像地址
     */
    private String avatar;

    /**
     * 角色标识: USER-普通用户, ADMIN-管理员
     */
    private String role;

    /**
     * 账号状态: 0-禁用, 1-正常
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}