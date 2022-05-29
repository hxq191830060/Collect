mvn clean

mvn install -N

cd common
mvn install

cd ../publish-service-api
mvn install

cd ../test-service-api
mvn install

cd ../user-service-api
mvn install

cd ../http-service
mvn compile jib:build -Dprofile=production -DprojectVersion=$1

cd ../publish-service
mvn compile jib:build -Dprofile=production -DprojectVersion=$1

cd ../test-service
mvn compile jib:build -Dprofile=production -DprojectVersion=$1

cd ../user-service
mvn compile jib:build -Dprofile=production -DprojectVersion=$1

#更新k8s中的镜像版本
curl -X PUT \
    -H "content-type: application/json" \
    -H "Cookie: KuboardUsername=promise; KuboardAccessKey=78sih3nnxp28.ammdybsx54cjf3z5pj384sh4r7emwi25" \
    -d '{"kind":"deployments","namespace":"promise","name":"http-service-deploy","images":{"registry.cn-hangzhou.aliyuncs.com/homework-promise/http-service":"registry.cn-hangzhou.aliyuncs.com/homework-promise/http-service:'$1'"}}' \
    "http://150.158.158.176:30080/kuboard-api/cluster/default/kind/CICDApi/promise/resource/updateImageTag"

curl -X PUT \
    -H "content-type: application/json" \
    -H "Cookie: KuboardUsername=promise; KuboardAccessKey=78sih3nnxp28.ammdybsx54cjf3z5pj384sh4r7emwi25" \
    -d '{"kind":"deployments","namespace":"promise","name":"publish-service-deploy","images":{"registry.cn-hangzhou.aliyuncs.com/homework-promise/publish-service":"registry.cn-hangzhou.aliyuncs.com/homework-promise/publish-service:'$1'"}}' \
    "http://150.158.158.176:30080/kuboard-api/cluster/default/kind/CICDApi/promise/resource/updateImageTag"

curl -X PUT \
    -H "content-type: application/json" \
    -H "Cookie: KuboardUsername=promise; KuboardAccessKey=78sih3nnxp28.ammdybsx54cjf3z5pj384sh4r7emwi25" \
    -d '{"kind":"deployments","namespace":"promise","name":"test-service-deploy","images":{"registry.cn-hangzhou.aliyuncs.com/homework-promise/test-service":"registry.cn-hangzhou.aliyuncs.com/homework-promise/test-service:'$1'"}}' \
    "http://150.158.158.176:30080/kuboard-api/cluster/default/kind/CICDApi/promise/resource/updateImageTag"

curl -X PUT \
    -H "content-type: application/json" \
    -H "Cookie: KuboardUsername=promise; KuboardAccessKey=78sih3nnxp28.ammdybsx54cjf3z5pj384sh4r7emwi25" \
    -d '{"kind":"deployments","namespace":"promise","name":"user-service-deploy","images":{"registry.cn-hangzhou.aliyuncs.com/homework-promise/user-service":"registry.cn-hangzhou.aliyuncs.com/homework-promise/user-service:'$1'"}}' \
    "http://150.158.158.176:30080/kuboard-api/cluster/default/kind/CICDApi/promise/resource/updateImageTag"
