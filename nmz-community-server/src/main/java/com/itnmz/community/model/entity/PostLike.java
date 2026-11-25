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
 * 点赞明细表
 * @TableName nmz_post_like
 */
@TableName(value ="nmz_post_like")
@Data
@Builder
//无参构造方法(Jackson 反序列化 JSON（接收前端 @RequestBody）或 MyBatis-Plus 反射实例化对象时报错)
@NoArgsConstructor
//@Builder 依赖全参构造
@AllArgsConstructor
public class PostLike {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 点赞人用户ID
     */
    private Long userId;

    /**
     * 点赞类型: 1-帖子, 2-评论
     */
    private Integer targetType;

    /**
     * 目标ID(帖子ID或评论ID)
     */
    private Long targetId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}