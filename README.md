[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java17](https://img.shields.io/badge/java-17-blue)](https://img.shields.io/badge/java-17-blue)
[![Gradle](https://img.shields.io/badge/gradle-v7.3-blue)](https://img.shields.io/badge/gradle-v7.3-blue)
[![Build and Test](https://github.com/jveverka/authx-micro/actions/workflows/main.yml/badge.svg)](https://github.com/jveverka/authx-micro/actions/workflows/main.yml)

# authx-micro
Minimalistic standalone OAuth2 authentication and authorization server. Project is compliant with subset of OpenID-connect and OAuth2 and other related specifications.

__Disclaimer:__ This project is __WIP__ ! First release is expected __EOF Dec.2021__. 
AuthX is simplified version of [iam-service](https://github.com/jveverka/iam-service) supporting more data access scenarios.

## Supported OAuth2 flows
* __Password Credentials__ - wip
* __Client Credentials__ - wip
* __Refresh Token__ - wip

### Endpoints
```
http://localhost:7777/actuator/health
http://localhost:7777/actuator/prometheus
```
 
### Technical documentation
* [Security Model](docs/authx-security-model.md)
* [Build and run locally.](docs/authx-build-and-run.md)
* [Internal Data Model.](docs/authx-data-model.md)
* [Authx in Docker.](docs/authx-dockerization.md)
