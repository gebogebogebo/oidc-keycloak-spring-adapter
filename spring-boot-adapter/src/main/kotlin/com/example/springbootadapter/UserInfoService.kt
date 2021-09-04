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

        val info = UserInfo()
        info.username = accessToken.preferredUsername
        info.emailID = accessToken.email
        info.lastname = accessToken.familyName
        info.firstname = accessToken.givenName
        info.realmName = accessToken.issuer

        val realmAccess = accessToken.realmAccess
        info.roles = realmAccess.roles.toString();
        info.scopes = accessToken.scope;

        return info
    }

    class UserInfo {
        var username: String? = "No data"
        var emailID: String? = "No data"
        var lastname: String? = "No data"
        var firstname: String? = "No data"
        var realmName: String? = "No data"
        var roles: String? = "No data"
        var scopes: String? = "No data"
    }
}