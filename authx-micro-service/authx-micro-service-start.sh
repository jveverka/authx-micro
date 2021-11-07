#!/bin/bash

function check_var() {
  PRINT=$1
  NAME=$2
  VALUE=$3
  if [ "${VALUE}" == "" ]; then
    echo "ERROR: EVN Variable ${NAME} is not set !";
    exit 1
  else
    if [ "${PRINT}" == "print" ]; then
      echo "${NAME}=${VALUE}"
    fi
  fi
}

echo "starting authx-micro as ${USER} ..."
echo "APP_CONFIG_PATH=${AUTHX_CONFIG_PATH}"
echo "JVM_OPTS=${JVM_OPTS}"
echo "SERVER_PORT=${SERVER_PORT}"
echo "MANAGEMENT_PORT=${MANAGEMENT_PORT}"
echo "XMX=${XMX}"

check_var print   REDIS_HOST ${REDIS_HOST}
check_var print   REDIS_PORT ${REDIS_PORT}
check_var print   MONGODB_HOST ${MONGODB_HOST}
check_var print   MONGODB_PORT ${MONGODB_PORT}
check_var print   MONGODB_DB ${MONGODB_DB}
check_var print   MONGODB_USERNAME ${MONGODB_USERNAME}
check_var noprint MONGODB_PASSWORD ${MONGODB_PASSWORD}

echo "Waiting for MongoDB to launch on ${MONGODB_HOST}:${MONGODB_PORT} ..."
while ! nc -z ${MONGODB_HOST} ${MONGODB_PORT}; do
  sleep 0.5 # wait for 0.5s before check again
  echo -n "."
done
echo ""
echo "MongoDB launched !"

if [ "${AUTHX_CONFIG_PATH}" = "false" ]; then
  echo "using default configuration"
  echo "SERVER_PORT=${SERVER_PORT}"
  echo "MANAGEMENT_PORT=${MANAGEMENT_PORT}"
  echo "XMX=${XMX}"
  java -Xms32m -Xmx${XMX} ${JVM_OPTS} -Djava.security.egd=file:/dev/./urandom \
     -Dspring.profiles.active=cloud -jar /authx-micro-service.jar
else
  echo "using custom configuration"
  echo "APP_CONFIG_PATH=${AUTHX_CONFIG_PATH}"
  echo "XMX=${XMX}"
  java -Xms32m -Xmx${XMX} ${JVM_OPTS} -Djava.security.egd=file:/dev/./urandom \
     -jar /authx-micro-service.jar \
     --spring.config.location=file:${AUTHX_CONFIG_PATH}
fi
