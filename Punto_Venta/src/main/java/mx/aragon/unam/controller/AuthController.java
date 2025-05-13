package mx.aragon.unam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController{
    @GetMapping("/login")
    public String login() {
        return "vistas/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "vistas/signup";
    }
}
