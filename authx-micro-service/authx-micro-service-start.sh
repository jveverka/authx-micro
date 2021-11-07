#!/bin/bash

echo "starting authx-micro as ${USER} ..."
echo "APP_CONFIG_PATH=${AUTHX_CONFIG_PATH}"
echo "JVM_OPTS=${JVM_OPTS}"

echo "Waiting for MongoDB to launch on ${AUTHX_MONGODB_HOST}:${AUTHX_MONGODB_PORT} ..."
while ! nc -z ${AUTHX_MONGODB_HOST} ${AUTHX_MONGODB_PORT}; do
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
