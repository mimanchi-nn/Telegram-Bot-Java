# 项目介绍
  项目是基于Telegram Bot API 开发的一个群组管理机器人，用于管理群组中的成员、积分、签到等功能。
## 项目架构
  - 基于Spring Boot 框架开发
  - 数据库采用MySQL 存储
  - 采用Telegram Bot API 与Telegram 进行交互
  - 采用Restful API 提供接口
  - 缓存目前使用本地缓存(修改接口即可,这里不使用Redis是预算不足)
  - 涉及到消息异步的地方使用Spring 提供的event事件机制,不使用消息队列还是因为预算不足

## 项目模块
  - telegram-dependency 模块: 用于定义项目中使用的依赖版本
  - telegram-bus-module 模块: 具体的业务逻辑
  - telegram-bot-admin-server 模块: 后台管理系统

## 功能介绍
  - 管理群组中的成员
  - 积分系统
  - 签到系统
  - 敏感词过滤
  - 自定义命令
  - 其他功能

## 安装部署
    - 安装Java 8 版本
    - 安装MySQL 5.7 及以上版本
    - 克隆项目到本地
    - 配置数据库连接信息
    - 配置Telegram Bot API Token
    - 打包项目
    - 运行项目
