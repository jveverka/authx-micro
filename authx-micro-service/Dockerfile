ARG ARCH="amd64"
FROM ${ARCH}/amazoncorretto:17-alpine3.14

COPY build/libs/authx-micro-service-*-RELEASE.jar /authx-micro-service.jar
COPY authx-micro-service-start.sh /authx-micro-service-start.sh
COPY src/main/resources/application.yml /application.yml
COPY src/main/resources/application-cloud.yml /application-cloud.yml

RUN apk add --no-cache netcat-openbsd

RUN chmod +x /authx-micro-service-start.sh

RUN adduser -g authxuser --disabled-password authxuser
USER authxuser

ENV XMX 128m
ENV SERVER_PORT 8080
ENV MANAGEMENT_PORT 7777
ENV ADMIN_USER_PASSWORD secret
ENV ADMIN_CLIENT_SECRET top-secret
ENV AUTHX_APP_ID authx-001
ENV AUTHX_CONFIG_PATH false
ENV JVM_OPTS ""
ENV REDIS_PORT 6379
ENV MONGODB_PORT 27017
ENTRYPOINT ["/authx-micro-service-start.sh"]
