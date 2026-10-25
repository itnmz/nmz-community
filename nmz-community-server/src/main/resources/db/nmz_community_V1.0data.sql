-- 1. 初始化管理员用户 (账号: admin, 初始密码: 123456 的 BCrypt 密文)
-- INSERT INTO `nmz_user` (`id`, `username`, `password`, `nickname`, `avatar`, `role`, `status`)
-- VALUES 
-- (1, 'admin', '$2a$10$7JB720yubVSZvVD0kmupSeOQ30.M5m0m/k56sYg2/5/QY/rD5pT3S', '社区管理员', 
-- 	'https://api.dicebear.com/7.x/identicon/svg?seed=admin', 'ADMIN', 1);
INSERT INTO `nmz_user` (`id`, `username`, `password`, `nickname`, `avatar`, `role`, `status`)
VALUES 
(1, 'admin', '$2a$10$7JB720yubVSZvVD0kmupSeOQ30.M5m0m/k56sYg2/5/QY/rD5pT3S', '社区管理员', 
	'https://ibb.co/gLCBJrF3', 'ADMIN', 1);

-- 2. 初始化核心板块分类
INSERT INTO `nmz_category` (`id`, `name`, `icon`, `description`)
VALUES 
(1, '英雄联盟', '🎮', '年年最有希望的一年，云教练指定分赛区，今天你尽力了吗？'),
(2, '原神', '☁️', '玩原神玩的！原神怎么你了？蒸馍，你不服气吗？原神，启动！'),
(3, '崩坏：星穹铁道', '🚂', '回合制仙人与赛博盆栽专区，上线挂机两分钟，下线赛博对线两小时。');

-- 3. 初始化 1 条测试帖子 (用于验证列表和详情查询)
INSERT INTO `nmz_post` (`id`, `user_id`, `category_id`, `title`, `content`, `view_count`, `like_count`, `favorite_count`, `comment_count`, `status`)
VALUES 
(1, 1, 1, '欢迎来到 奈美子 社区！', '# Hello Community\n\n这是系统自动生成的第一篇测试帖子，项目后端环境已成功就绪！', 1, 0, 0, 0, 1);