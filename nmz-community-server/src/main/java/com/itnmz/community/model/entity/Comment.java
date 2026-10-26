package com.itnmz.community.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 社区评论表
 * @TableName nmz_comment
 */
@TableName(value ="nmz_comment")
@Data
public class Comment {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所属帖子ID
     */
    private Long postId;

    /**
     * 发布人ID
     */
    private Long userId;

    /**
     * 父级评论ID(一级评论为NULL)
     */
    private Long parentId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论点赞数
     */
    private Integer likeCount;

    /**
     * 评论时间
     */
    private LocalDateTime createTime;
}