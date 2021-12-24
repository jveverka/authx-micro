# Dockerized Authx Service

### Build Docker Image
```
export AUTHX_VERSION=$(gradle getversion | grep AUTHX_VERSION | awk -F "=" '{ print $2 }')
export ARCH=amd64
#export ARCH=arm64v8

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

