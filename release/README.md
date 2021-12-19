# Creating AuthX release
1. Modify release version script ``version-upgrade.sh`` and run it.
   ``./release/version-upgrade.sh``
2. Run ``gradle clean build test`` continue if success.
3. Push release changes to git.
4. Publish libraries.
5. Publish docker images.
6. Create release branch / tag.
 