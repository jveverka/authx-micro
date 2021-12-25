# AuthX Service Deployments

### Docker Compose
```
curl https://raw.githubusercontent.com/jveverka/authx-micro/main/docs/authx-service-deployments/authx-docker-compose.yml -o authx-docker-compose.yml
docker-compose -f authx-docker-compose.yml up -d 
curl http://localhost:7777/actuator/health
curl http://localhost:8080/authx/api/v1/system/info
docker-compose -f authx-docker-compose.yml down -v --rmi all --remove-orphans
```

### Kubernetes
* Setup environment variables and secrets.
* Deployment
  ```
  kubectl apply -f authx-service-setup.yml
  kubectl apply -f authx-service-deployment.yml
  ```
* Undeployment
  ```
  kubectl delete deployment.apps/authx-service -n authx-service
  kubectl delete service/authx-service -n authx-service
  kubectl delete secret/authx-service -n authx-service
  kubectl delete namespace/authx-service
  ```

### Docker
```
docker run -name authx-service \
  -p 8080:8080 -p 7777:7777 \
  -e MONGODB_PASSWORD=s3cr3t \
  -e ADMIN_USER_PASSWORD=secret \
  -e ADMIN_CLIENT_SECRET=secret \ 
  -e ...
  jurajveverka/authx-micro-service:1.1.7-amd64
```