Token Controller
```diff
refreshToken
+ POST /token/

revokeRefreshToken
! PUT /token/  

validateToken
+ POST /token/validateToken
```

User Controller
```diff
signUp
+POST /user / 

updateUser
!PUT /user/ 

removeUser
-DELETE /user/{id}

signIn
+POST /user/signIn 
```
