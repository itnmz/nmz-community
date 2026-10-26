package com.itnmz.community.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 点赞明细表
 * @TableName nmz_post_like
 */
@TableName(value ="nmz_post_like")
@Data
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