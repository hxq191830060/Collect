docker build -t promise-recommend .
docker tag promise-recommend registry.cn-hangzhou.aliyuncs.com/homework-promise/promise-recommend:$1
docker push registry.cn-hangzhou.aliyuncs.com/homework-promise/promise-recommend:$1

#向Kubernetes发送HTTP请求，滚动更新