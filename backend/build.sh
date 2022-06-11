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
