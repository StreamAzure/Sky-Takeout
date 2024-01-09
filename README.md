# README

## 目录结构

- docs：含后端初始工程、菜品图片、项目接口文档（需导入 YApi 查看）、数据库设计文档
- sky-admin：管理端前端工程（网页），通过 docker-compose 启动
- sky-user：用户端前端工程（微信小程序），需下载微信开发者工具后导入该工程运行
- sky-backend：后端工程（Java Spring Boot）

## 开发环境部署

### 前端工程及数据库部署

通过以下命令启动：

```shell
docker-compose up -d
```

该命令将执行：

- 启动 MySQL 容器，版本 8.0.33，端口 3306；sky.sql 文件会在启动时自动执行，完成建库建表操作
- 启动 Redis 容器，版本 6.0，端口 6379
- 启动管理端前端工程，通过 localhost:80 访问

### 后端工程运行

需要自行编写 application-dev.yml，并放在 sky-backend/sky-server/src/main/resources 目录下。

application-dev.yml 示例：

```yaml
sky:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    host: localhost
    port: 3306
    database: sky_take_out
    username: root
    password: 123
  redis:
    host: localhost
    port: 6379
    database: 10
  # 阿里云 OSS 服务配置，根据实际自行填写字段
  # 若不配置则无法在项目中上传图片
  alioss:
    endpoint: 
    access-key-id: 
    access-key-secret: 
    bucket-name: 
  # 微信小程序配置，根据实际自行填写字段
  wechat:
    appid: 
    secret: 
```

### 项目接口文档查看

通过 YApi 导入并查看项目接口文档，有以下两种方式，任选一种即可：

- 在线 YApi：https://yapi.pro/
- 通过 docker 本地部署 YAPi：https://github.com/fjc0k/docker-YApi

### Swagger 在线接口测试

Swagger 是由 Spring Boot 启动的，需要先运行后端工程才能访问：http://localhost:8080/doc.html 

