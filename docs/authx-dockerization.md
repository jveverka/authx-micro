# Authx in Docker

### Build Docker Image
```
#export AUTHX_VERSION=$authXVersion
#export ARCH="amd64"
#export ARCH="arm64v8"

docker build -t jurajveverka/authx-micro-service:${AUTHX_VERSION}-${ARCH} \
  --build-arg ARCH=${ARCH} \
  -f authx-micro-service/Dockerfile ./authx-micro-service
  
docker push jurajveverka/authx-micro-service:${AUTHX_VERSION}-${ARCH}

docker manifest create \
jurajveverka/authx-micro-service:${AUTHX_VERSION} \
--amend jurajveverka/authx-micro-service:${AUTHX_VERSION}-amd64 \
--amend jurajveverka/authx-micro-service:${AUTHX_VERSION}-arm64v8

docker manifest push jurajveverka/authx-micro-service:${VERSION}
```

### Start Standalone Docker
```
docker run -d --name authx-micro-service \
  -p 8080:8080 -p 7777:7777 \
  -e "REDIS_HOST=127.0.0.1" \
  -e "REDIS_PORT=6379" \
  jurajveverka/authx-micro-service:${AUTHX_VERSION}-${ARCH}
```

### Start With docker-compose
```
docker-compose -f authx-micro-service/authx-docker-compose.yml up -d
docker-compose -f authx-micro-service/authx-docker-compose-test.yml down -v
```
