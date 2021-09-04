package com.example.springbootadapter

import org.keycloak.KeycloakPrincipal
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import javax.servlet.http.HttpServletRequest

class UserInfoService {
    fun getUserInfo(request: HttpServletRequest):UserInfo{
        val token = request.userPrincipal as KeycloakAuthenticationToken
        val principal = token.principal as KeycloakPrincipal<*>
        val session = principal.keycloakSecurityContext
        val accessToken = session.token
        val idToken = session.idToken
        val info = UserInfo()
        info.username = idToken.preferredUsername?:""
        info.email = idToken.email?:""
        info.lastname = idToken.familyName?:""
        info.firstname = idToken.givenName?:""
        info.idTokenInfo.issuer = idToken.issuer?:""
        info.idTokenInfo.audience = idToken.audience?.joinToString(",")?:""
        info.accessTokenInfo.issuer = accessToken.issuer?:""
        info.accessTokenInfo.audience = accessToken.audience?.joinToString(",")?:""
        val realmAccess = accessToken.realmAccess
        info.accessTokenInfo.roles = realmAccess.roles?.toString()?:""
        info.accessTokenInfo.scopes = accessToken.scope?:""
        return info
    }

    class UserInfo {
        var username = "No data"
        var email = "No data"
        var lastname = "No data"
        var firstname = "No data"
        var idTokenInfo = IdTokenInfo()
        var accessTokenInfo = AccessTokenInfo()
    }

    class IdTokenInfo {
        var issuer = "No data"
        var audience = "No data"
    }

    class AccessTokenInfo {
        var issuer = "No data"
        var audience = "No data"
        var roles = "No data"
        var scopes = "No data"
    }

}