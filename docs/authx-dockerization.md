# Authx in Docker

### Build Docker Image
```
#export VERSION=1.0.0-RELEASE
#export ARCH="amd64"
#export ARCH="arm64v8"

docker build -t jurajveverka/authx-micro-service:${VERSION}-${ARCH} \
  --build-arg ARCH=${ARCH} \
  -f authx-micro-service/Dockerfile ./authx-micro-service
  
docker push jurajveverka/authx-micro-service:${VERSION}-${ARCH}

docker manifest create \
jurajveverka/authx-micro-service:${VERSION} \
--amend jurajveverka/authx-micro-service:${VERSION}-amd64 \
--amend jurajveverka/authx-micro-service:${VERSION}-arm64v8

docker manifest push jurajveverka/authx-micro-service:${VERSION}
```

### Start Standalone Docker
```
docker run -d --name authx-micro-service \
  -p 8080:8080 -p 7777:7777 \
  -e "REDIS_HOST=127.0.0.1" \
  -e "REDIS_PORT=6379" \
  jurajveverka/authx-micro-service:${VERSION}-${ARCH}
```

### Start With docker-compose
```
docker-compose -f authx-micro-service/docker-compose-test.yml up -d
docker-compose -f authx-micro-service/docker-compose-test.yml down -v
```
