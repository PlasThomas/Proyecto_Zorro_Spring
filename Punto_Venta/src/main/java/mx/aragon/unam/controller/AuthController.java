package mx.aragon.unam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthController{
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String inicio(){
        return "vistas/index";
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login() {
        return "vistas/login";
    }
}
