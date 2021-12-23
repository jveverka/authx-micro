# Creating AuthX release

0. Clone this repository.
1. Update release ``authXVersion`` in: 
   * [build.gradle](../build.gradle)
   * [authx-docker-compose.yml](authx-service-deployments/authx-docker-compose.yml)
   * [README.md](authx-service-deployments/README.md)
   * [authx-service-deployment.yml](authx-service-deployments/k8s/authx-service-deployment.yml)
2. Export environment variable ``AUTHX_VERSION``
   ```
   export AUTHX_VERSION=$authXVersion
   ```
3. Run ``gradle clean build test`` continue if success.
4. Push release changes to git ``main`` branch.
5. Publish libraries.
   ```
   gradle publish
   ``` 
6. Build and publish docker images as described [here](authx-dockerization.md).
7. Create release tag.
   ```
   git tag -l 
   git tag v${AUTHX_VERSION}
   git push origin --tags  
   ``` 
8. Update github [release page](https://github.com/jveverka/authx-micro/releases).
9. Done.
 