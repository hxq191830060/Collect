yarn install
yarn build
docker pull nginx
docker build -t vueapp1.0 .
docker tag vueapp1.0 registry.cn-hangzhou.aliyuncs.com/homework-promise/frontend-crowdsource:$1
docker push registry.cn-hangzhou.aliyuncs.com/homework-promise/frontend-crowdsource:$1

#向Kubernetes发送HTTP请求，进行滚动更新
