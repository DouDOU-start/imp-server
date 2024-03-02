# 使用官方Go镜像作为构建环境
FROM golang AS build

# 设置代理服务器
ENV GOPROXY=https://goproxy.cn,direct

# 设置工作目录
WORKDIR /app

# 复制源代码到容器内，包含 mod和 sum文件
COPY . .

# 下载依赖
RUN go mod download

# 编译Go程序
RUN CGO_ENABLED=0 GOOS=linux go build -o /myapp

# 使用scratch作为最终运行时镜像
FROM scratch

# 从构建阶段复制可执行文件
COPY --from=build /myapp /myapp

# 运行可执行文件
ENTRYPOINT ["/myapp"]