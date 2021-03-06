# AuthX Service Deployments

### Docker Compose
```
curl https://raw.githubusercontent.com/jveverka/authx-micro/main/docs/authx-service-deployments/authx-docker-compose.yml -o authx-docker-compose.yml
docker-compose -f authx-docker-compose.yml up -d 
curl http://localhost:7777/actuator/health
curl http://localhost:8080/authx/api/v1/system/info
docker-compose -f authx-docker-compose.yml down -v --rmi all --remove-orphans
```

### Docker Swarm
* Consider Swarm cluster: ``swarm-master, swarm-mode-01, swarm-mode-02``
* On Swarm Master node:
  ```
  docker network create --attachable -d overlay authx-network
  docker node update --label-add network-proxy=true swarm-node-01
  docker node update --label-add persistence=true swarm-node-02
  ```
* On Swarm Master node - deploy nginx authx and data layer
  ```
  sudo docker stack deploy -c authx-swarm-databases.yml authx-databases
  sudo docker stack deploy -c authx-swarm-app.yml authx-app
  sudo docker stack deploy -c authx-swarm-network.yml authx-network
  ```
* Check deployments
  ```
  docker stack ls
  docker stack ps authx-databases
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
  jurajveverka/authx-micro-service:1.2.0-amd64
```