#!/bin/bash

source ~/.bash_profile

cd ..

APP_NAME=helper-dev
REPOSITORY=`pwd`
RELEASE=dev

# 저장소로부터 소스를 업데이트 받습니다.
git pull

# maven 빌드와 jar 파일 패키징 작업을 합니다. (작업 완료시 .jar파일 생성)
sh mvnw package

# .jar 파일 타겟팅
JAR_NAME=$(ls $REPOSITORY/target | grep jar | head -n 1)
JAR_PATH=$REPOSITORY/target/$JAR_NAME

# 현재 실행중인 서버가 있으면 잡아서 종료 시킵니다.
CURRENT_PID=$(pgrep -f $APP_NAME)
if [ -z $CURRENT_PID ]
then
  echo ">>>> java process not found."
else
  echo ">>>> PID: $CURRENT_PID kill."
  kill -15 $CURRENT_PID
  sleep 15
fi

# .jar 파일 java 실행합니다.
echo ">>>> $JAR_PATH java execute."
nohup java -jar $JAR_PATH --spring.config.location=classpath:/application.yaml --spring.profiles.active=$RELEASE > /dev/null 2> /dev/null < /dev/null &
sleep 5
CURRENT_PID=$(pgrep -f $APP_NAME)
echo ">>>> New PID: $CURRENT_PID"

curl -X POST -H 'Content-type: application/json' --data '{"text":"42Helper-dev '$CURRENT_PID' finished!"}' $HELPER42_WEBHOOK
