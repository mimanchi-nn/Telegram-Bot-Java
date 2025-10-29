CREATE TABLE `bot_group_check_in_record` (
                                             `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                             `group_id` bigint NOT NULL COMMENT '群ID（Telegram群唯一标识）',
                                             `user_id` bigint NOT NULL COMMENT '用户ID（Telegram用户唯一标识）',
                                             `user_name` varchar(100)  DEFAULT NULL COMMENT '用户名（展示用）',
                                             `today_check_in_time` timestamp NULL DEFAULT NULL COMMENT '今日签到时间（NULL表示今日未签）',
                                             `total_check_in_days` int NOT NULL DEFAULT '0' COMMENT '累计签到天数',
                                             `continuous_check_in_days` int NOT NULL DEFAULT '0' COMMENT '连续签到天数',
                                             `last_check_in_date` date DEFAULT NULL COMMENT '最后一次签到日期（用于判断连续签到）',
                                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
                                             `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
                                             PRIMARY KEY (`id`),
                                             UNIQUE KEY `uk_group_user` (`group_id`,`user_id`),
                                             KEY `idx_group_id` (`group_id`) COMMENT '群ID索引，优化查询',
                                             KEY `idx_user_id` (`user_id`) COMMENT '群ID索引，优化查询'
)AUTO_INCREMENT=1  COMMENT='群聊签到记录表';


CREATE TABLE `bot_group_manage_info` (
                                         `id` int NOT NULL AUTO_INCREMENT,
                                         `group_id` bigint DEFAULT '0' COMMENT '群组ID',
                                         `bot_id` bigint DEFAULT '0' COMMENT '机器人ID',
                                         `group_name` varchar(255) DEFAULT NULL COMMENT '群组名称',
                                         `group_type` varchar(128) DEFAULT NULL COMMENT '群组类型',
                                         `inviter_id` bigint DEFAULT NULL COMMENT '邀请人ID',
                                         `role` varchar(128) DEFAULT NULL COMMENT '用户角色',
                                         `inviter_first_name` varchar(128) DEFAULT NULL COMMENT '邀请人 firstName',
                                         `inviter_last_name` varchar(128) DEFAULT NULL COMMENT '邀请人 lastName',
                                         `inviter_username` varchar(128) DEFAULT NULL COMMENT '邀请人 username',
                                         `invite_time` bigint DEFAULT NULL COMMENT '邀请时间',
                                         `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
                                         `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
                                         PRIMARY KEY (`id`) USING BTREE,
                                         KEY `idx_group_id` (`group_id`),
                                         KEY `idx_bot_id` (`bot_id`)
)AUTO_INCREMENT=1  COMMENT='机器人群组管理';

CREATE TABLE `bot_group_member_info` (
                                         `id` int NOT NULL AUTO_INCREMENT,
                                         `group_id` bigint DEFAULT '0' COMMENT '群组ID',
                                         `bot_id` bigint DEFAULT '0' COMMENT '机器人ID',
                                         `member_id` bigint DEFAULT '0' COMMENT '群成员ID',
                                         `group_name` varchar(255) DEFAULT NULL COMMENT '群组名称',
                                         `member_first_name` varchar(128) DEFAULT NULL COMMENT '群成员 firstName',
                                         `member_last_name` varchar(128) DEFAULT NULL COMMENT '群成员 lastName',
                                         `member_username` varchar(128) DEFAULT NULL COMMENT '群成员 username',
                                         `inviter_id` bigint DEFAULT NULL COMMENT '邀请人ID',
                                         `role` varchar(128) DEFAULT NULL COMMENT '用户角色',
                                         `inviter_first_name` varchar(128) DEFAULT NULL COMMENT '邀请人 firstName',
                                         `inviter_last_name` varchar(128) DEFAULT NULL COMMENT '邀请人 lastName',
                                         `inviter_username` varchar(128) DEFAULT NULL COMMENT '邀请人 username',
                                         `invite_time` bigint DEFAULT NULL COMMENT '邀请时间',
                                         `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
                                         `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
                                         PRIMARY KEY (`id`) USING BTREE,
                                         KEY `idx_group_id` (`group_id`),
                                         KEY `idx_bot_id` (`bot_id`)
) COMMENT='群组群成员管理';


CREATE TABLE `bot_message_info` (
                                    `id` int NOT NULL AUTO_INCREMENT,
                                    `group_id` bigint DEFAULT '0' COMMENT '群组ID',
                                    `message_id` bigint DEFAULT '0' COMMENT '消息ID',
                                    `member_id` bigint DEFAULT '0' COMMENT '群成员ID',
                                    `send_time` bigint DEFAULT '0' COMMENT '发送时间',
                                    `content` text COMMENT '发送内容',
                                    `send_first_name` varchar(128) DEFAULT NULL COMMENT '邀请人 firstName',
                                    `send_last_name` varchar(128) DEFAULT NULL COMMENT '邀请人 lastName',
                                    `send_username` varchar(128) DEFAULT NULL COMMENT '邀请人 username',
                                    `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
                                    `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    KEY `idx_group_id` (`group_id`),
                                    KEY `idx_message_id` (`message_id`),
                                    KEY `idx_member_id` (`member_id`)
)AUTO_INCREMENT=1  COMMENT='群组消息管理';


CREATE TABLE `config_sensitive_word` (
                                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                         `group_id` bigint NOT NULL COMMENT '群ID（Telegram群唯一标识）',
                                         `word` text NOT NULL COMMENT '敏感词',
                                         `process_type` int NOT NULL DEFAULT '0' COMMENT '敏感词处理方式',
                                         `warn_message` text NOT NULL COMMENT '警告内容',
                                         `status` int NOT NULL DEFAULT '0' COMMENT '是否开启',
                                         `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
                                         `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
                                         PRIMARY KEY (`id`),
                                         KEY `idx_group_id` (`group_id`) COMMENT '群ID索引，优化查询'
)AUTO_INCREMENT=1  COMMENT='敏感词配置';


CREATE TABLE `group_point_limit` (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                     `group_id` bigint NOT NULL COMMENT '群ID（Telegram群唯一标识）',
                                     `daily_max_point` bigint NOT NULL COMMENT '每日上限积分',
                                     `speak_point_per_valid` bigint NOT NULL COMMENT '有效发言（超4字）得分数',
                                     `checkin_point` bigint NOT NULL COMMENT '签到分数',
                                     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
                                     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
                                     PRIMARY KEY (`id`),
                                     KEY `idx_group_id` (`group_id`) COMMENT '群ID索引，优化查询'
)AUTO_INCREMENT=1  COMMENT='群积分配置表';


CREATE TABLE `member_point_record` (
                                       `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                       `group_id` bigint NOT NULL COMMENT '群ID（Telegram群唯一标识）',
                                       `member_id` bigint NOT NULL COMMENT '用户id',
                                       `point` bigint NOT NULL COMMENT '积分',
                                       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
                                       `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
                                       PRIMARY KEY (`id`),
                                       KEY `idx_group_id` (`group_id`) COMMENT '群ID索引，优化查询',
                                       KEY `idx_member_id` (`member_id`) COMMENT '用户id'
)AUTO_INCREMENT=1  COMMENT='用户积分表';