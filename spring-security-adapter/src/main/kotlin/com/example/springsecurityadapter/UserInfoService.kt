package com.example.springsecurityadapter

class UserInfoService {
    fun getUserInfo():UserInfo{
        val info = UserInfo()
        return info
    }

    class UserInfo {
        var username = "No data"
        var emailID = "No data"
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