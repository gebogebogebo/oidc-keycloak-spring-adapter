package com.example.springbootadapter

class UserInfoService {
    fun getUserInfo():UserInfo{
        return UserInfo()
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