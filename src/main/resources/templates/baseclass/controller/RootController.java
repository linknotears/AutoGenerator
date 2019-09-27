package com.linkey.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("${package.Controller}.RootController")
@RequestMapping("/")
public class RootController {
    @RequestMapping()
    public String index() throws Exception{
        return "index";
    }

    @RequestMapping("/index")
    public String index(String param) throws Exception{
        return "index";
    }

    @RequestMapping("/login")
    public String login(String param) throws Exception{
        return "login";
    }

    @RequestMapping("/admin")
    public String adminIndex(String param) throws Exception{
        return "redirect:/${cfg.loginTable}/index";
    }

    @RequestMapping(value = "/admin/login",method = {RequestMethod.GET})
    public String adminLogin(String param) throws Exception{
        return "admin/login";
    }
}
