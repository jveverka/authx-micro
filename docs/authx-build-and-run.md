### Requirements
* Java 17 or later.
* Gradle 7.x or later.
* Docker 20.x or later.

### Build and Run
```
gradle clean build test
```

### Check Dependencies
```
gradle :authx-micro-service:dependencies
gradle :authx-micro-common:dependencies
gradle :authx-micro-clients:authx-micro-jclient:dependencies
gradle :authx-micro-clients:authx-micro-jredis:dependencies
```