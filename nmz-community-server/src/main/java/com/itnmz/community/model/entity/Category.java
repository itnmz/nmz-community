package com.itnmz.community.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 帖子分类板块表
 * @TableName nmz_category
 */
@TableName(value ="nmz_category")
@Data
@Builder
//无参构造方法(Jackson 反序列化 JSON（接收前端 @RequestBody）或 MyBatis-Plus 反射实例化对象时报错)
@NoArgsConstructor
//@Builder 依赖全参构造
@AllArgsConstructor
public class Category {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类图标(图片URL/图标组件名/Emoji)
     */
    private String icon;

    /**
     * 分类描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}