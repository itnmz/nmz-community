-- 数据库nmz_community
-- 1. 用户表 (nmz_user)
DROP TABLE IF EXISTS `nmz_user`;
CREATE TABLE `nmz_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(32) NOT NULL COMMENT '登录用户名/账号',
    `password` VARCHAR(128) NOT NULL COMMENT 'BCrypt加密密码',
    `nickname` VARCHAR(50) NOT NULL COMMENT '用户昵称',
    `avatar` VARCHAR(255) DEFAULT '' COMMENT '用户头像地址',
    `role` VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT '角色标识: USER-普通用户, ADMIN-管理员',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '账号状态: 0-禁用, 1-正常',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) COMMENT='用户表';


-- 2. 分类表 (nmz_category)
DROP TABLE IF EXISTS `nmz_category`;
CREATE TABLE `nmz_category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `description` VARCHAR(255) DEFAULT '' COMMENT '分类描述',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) COMMENT='帖子分类板块表';
--补充字段（分类图标）
ALTER TABLE `nmz_category` 
ADD COLUMN `icon` VARCHAR(255) DEFAULT '' COMMENT '分类图标(图片URL/图标组件名/Emoji)' 
AFTER `name`;


-- 3. 帖子表 (nmz_post)
DROP TABLE IF EXISTS `nmz_post`;
CREATE TABLE `nmz_post` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '作者ID',
    `category_id` BIGINT NOT NULL COMMENT '所属分类ID',
    `title` VARCHAR(128) NOT NULL COMMENT '帖子标题',
    `content` LONGTEXT NOT NULL COMMENT '帖子Markdown内容',
    `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览量',
    `like_count` INT NOT NULL DEFAULT 0 COMMENT '点赞数',
    `favorite_count` INT NOT NULL DEFAULT 0 COMMENT '收藏数',
    `comment_count` INT NOT NULL DEFAULT 0 COMMENT '评论数',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-草稿, 1-已发布, 2-违规下架',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`)
) COMMENT='社区帖子表';


-- 4. 帖子评论表 (nmz_comment)
DROP TABLE IF EXISTS `nmz_comment`;
CREATE TABLE `nmz_comment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `post_id` BIGINT NOT NULL COMMENT '所属帖子ID',
    `user_id` BIGINT NOT NULL COMMENT '发布人ID',
    `parent_id` BIGINT DEFAULT NULL COMMENT '父级评论ID(一级评论为NULL)',
    `content` VARCHAR(1000) NOT NULL COMMENT '评论内容',
    `like_count` INT NOT NULL DEFAULT 0 COMMENT '评论点赞数',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
    PRIMARY KEY (`id`)
) COMMENT='社区评论表';


-- 5. 帖子点赞记录表 (nmz_like)
DROP TABLE IF EXISTS `nmz_like`;
CREATE TABLE `nmz_like` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '点赞人用户ID',
    `target_type` TINYINT NOT NULL COMMENT '点赞类型: 1-帖子, 2-评论',
    `target_id` BIGINT NOT NULL COMMENT '目标ID(帖子ID或评论ID)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_target` (`user_id`, `target_type`, `target_id`)
) COMMENT='点赞明细表';
RENAME TABLE `nmz_like` TO `nmz_post_like`;


-- 6. 帖子收藏记录表 (nmz_favorite)
DROP TABLE IF EXISTS `nmz_favorite`;
CREATE TABLE `nmz_favorite` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '收藏人用户ID',
    `post_id` BIGINT NOT NULL COMMENT '被收藏的帖子ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_post` (`user_id`, `post_id`)
) COMMENT='帖子收藏表';
RENAME TABLE `nmz_favorite` TO `nmz_post_favorite`;