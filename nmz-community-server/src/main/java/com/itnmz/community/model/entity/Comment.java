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
 * 社区评论表
 * @TableName nmz_comment
 */
@TableName(value ="nmz_comment")
@Data
@Builder
//无参构造方法(Jackson 反序列化 JSON（接收前端 @RequestBody）或 MyBatis-Plus 反射实例化对象时报错)
@NoArgsConstructor
//@Builder 依赖全参构造
@AllArgsConstructor
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