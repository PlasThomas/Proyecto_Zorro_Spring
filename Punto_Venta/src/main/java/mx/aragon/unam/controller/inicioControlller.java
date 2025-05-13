package mx.aragon.unam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class inicioControlller {
    @GetMapping("/")
    public String inicio(){
        return "vistas/index";
    }
}
