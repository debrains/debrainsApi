#!/usr/bin/env bash

REPOSITORY=/usr/local/debrainsApi
cd $REPOSITORY

APP_NAME=debrainsApi
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

echo "> Kill Previous Process" >> /usr/local/debrainsApi/deploy.log

if [ -z "$CURRENT_PID" ]
then
  echo "> No Process to Kill" >> /usr/local/debrainsApi/deploy.log
else
  echo "> Kill $CURRENT_PID" >> /usr/local/debrainsApi/deploy.log
  kill -15 "$CURRENT_PID" >> /usr/local/debrainsApi/deploy.log 2>&1
  sleep 5
fi

echo "> $JAR_PATH Deploy"
nohup java -jar $JAR_PATH >> /usr/local/debrainsApi/deploy.log 2>&1 &