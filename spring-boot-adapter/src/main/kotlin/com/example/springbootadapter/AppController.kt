package com.example.springbootadapter

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import javax.servlet.http.HttpServletRequest
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity







@Controller
class AppController {

    @GetMapping("/")
    fun index():String{
        return "index"
    }

    @GetMapping("/secure/welcome")
    fun welcome(model: Model):String{
        model.addAttribute("userinfo", UserInfoService().getUserInfo())
        return "welcome"
    }

    @GetMapping("/secure/logout")
    fun logout(request: HttpServletRequest):String{
        request.logout()
        return "redirect:/"
    }

    @GetMapping("/secure/setting")
    fun setting(model: Model):String{
        val response = template.getForEntity(endpoint, String::class.java)
        model.addAttribute("openidconfiguration", response.body)
        return "setting"
    }

    @Autowired
    private lateinit var template: KeycloakRestTemplate
    //private val endpoint = "http://localhost:8081/auth/realms/master/protocol/openid-connect/userinfo"
    private val endpoint = "http://localhost:8081/auth/realms/master/.well-known/openid-configuration"

}