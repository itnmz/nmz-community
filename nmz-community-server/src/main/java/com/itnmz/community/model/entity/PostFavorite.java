package com.itnmz.community.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 帖子收藏表
 * @TableName nmz_post_favorite
 */
@TableName(value ="nmz_post_favorite")
@Data
public class PostFavorite {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 收藏人用户ID
     */
    private Long userId;

    /**
     * 被收藏的帖子ID
     */
    private Long postId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}