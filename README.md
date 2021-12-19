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

### REST Endpoints
```
http://localhost:7777/actuator/health
http://localhost:7777/actuator/prometheus
```
 
### Technical documentation
* [Security Model](docs/authx-security-model.md)
* [Internal Data Model.](docs/authx-data-model.md)
* [Authx in Docker.](docs/authx-dockerization.md)
* [Build and run locally.](docs/authx-build-and-run.md)

#### RFCs and Specifications
* [RFC6749](https://tools.ietf.org/html/rfc6749) - OAuth 2.0 Authorization Framework
* [RFC7009](https://tools.ietf.org/html/rfc7009) - OAuth 2.0 Token Revocation
* [RFC7662](https://tools.ietf.org/html/rfc7662) - OAuth 2.0 Token Introspection
* [RFC6750](https://tools.ietf.org/html/rfc6750) - OAuth 2.0 Bearer Token Usage
* [RFC8414](https://tools.ietf.org/html/rfc8414) - OAuth 2.0 Authorization Server Metadata
* [RFC7636](https://tools.ietf.org/html/rfc7636) - OAuth 2.0 Proof Key for Code Exchange by OAuth Public Clients (PKCE)
* [RFC7519](https://tools.ietf.org/html/rfc7519) - JSON Web Token (JWT)
* [RFC7517](https://tools.ietf.org/html/rfc7517) - JSON Web Key (JWK)
* [OpenID](https://openid.net/specs/openid-connect-core-1_0.html) - OpenID Connect Core 1.0

