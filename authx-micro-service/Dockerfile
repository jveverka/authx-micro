ARG ARCH="amd64"
FROM ${ARCH}/eclipse-temurin:17-jdk-focal

COPY build/libs/authx-micro-service-*.jar /authx-micro-service.jar
COPY authx-micro-service-start.sh /authx-micro-service-start.sh
COPY src/main/resources/application.yml /application.yml
COPY src/main/resources/application-cloud.yml /application-cloud.yml

RUN apt update
RUN apt install -y netcat

RUN chmod +x /authx-micro-service-start.sh

RUN addgroup authxuser
RUN useradd -g authxuser -m authxuser
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
ENV CONTEXT_PATH "/authx"
ENV AUTHX_LOG_LEVEL INFO
ENV ROOT_LOG_LEVEL INFO
ENV AUTHX_INTERNAL_URL "http://localhost:8080/authx"
ENV AUTHX_EXTERNAL_URL "http://localhost:8080/authx"
ENTRYPOINT ["/authx-micro-service-start.sh"]
