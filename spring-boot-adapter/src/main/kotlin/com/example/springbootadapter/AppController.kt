package com.example.springbootadapter

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import javax.servlet.http.HttpServletRequest

@Controller
class AppController {

    @GetMapping("/")
    fun index():String{
        return "index"
    }

    @GetMapping("/secure/welcome")
    fun welcome(request: HttpServletRequest, model: Model):String{
        model.addAttribute("userinfo", UserInfoService().getUserInfo(request))
        return "welcome"
    }

    @GetMapping("/secure/logout")
    fun logout(request: HttpServletRequest):String{
        request.logout()
        return "redirect:/"
    }

    @GetMapping("/secure/setting")
    fun setting():String{
        return "setting"
    }
}