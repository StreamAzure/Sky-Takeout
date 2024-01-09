# README

## 目录结构

- docs：含后端初始工程、菜品图片、项目接口文档（需导入 YApi 查看）、数据库设计文档
- sky-admin：管理端前端工程（网页），通过 docker-compose 启动
- sky-user：用户端前端工程（微信小程序），需下载微信开发者工具后导入该工程运行
- sky-backend：后端工程（Java Spring Boot）

## 开发环境部署

通过以下命令启动：

```shell
docker-compose up -d
```

该命令将执行：

- 启动 MySQL 容器，版本 8.0.33，端口 3306；sky.sql 文件会在启动时自动执行，完成建库建表操作
- 启动 Redis 容器，版本 6.0，端口 6379
- 启动管理端前端工程，通过 localhost:80 访问

---

通过 YApi 导入并查看项目接口文档，有以下两种方式，任选一种即可：

- 在线 YApi：https://yapi.pro/
- 通过 docker 本地部署 YAPi：https://github.com/fjc0k/docker-YApi