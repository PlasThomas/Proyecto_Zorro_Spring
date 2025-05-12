package mx.aragon.unam.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class inicioAdminController {
    @GetMapping("/admin/inicio")
    public String login() {
        return "vistas/admin/inicio";  // Plantilla login.html
    }
}
