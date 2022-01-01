# Build native AuthX binary

```
sdk use java 21.3.0.r17-grl
sdk use gradle 7.3.3
gradle clean build -x test
gradle clean build nativeCompile -x test
```