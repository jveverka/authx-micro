# Creating AuthX release

0. Clone this repository.
1. Update release ``authXVersion`` in [build.gradle](../build.gradle)
2. Export environment variable ``AUTHX_VERSION``
   ```
   export AUTHX_VERSION=$authXVersion
   ```
3. Run ``gradle clean build test`` continue if success.
4. Push release changes to git ``main`` branch.
5. Publish libraries.
6. Publish docker images.
7. Create release branch / tag.
8. Update github release page.
9. Done.
 