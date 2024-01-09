# 使用官方 Nginx 镜像作为基础镜像
FROM nginx:latest

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

# 将当前目录下的 nginx.conf 拷贝到容器中的 /etc/nginx/nginx.conf
COPY nginx.conf /etc/nginx/nginx.conf

# 将当前目录下的 html 目录拷贝到容器中的 /usr/share/nginx/html
COPY html /usr/share/nginx/html