package mx.aragon.unam.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioAdminController {
    @GetMapping("/admin/inicio")
    public String vistaInicioAdmin() {
        return "vistas/admin/inicio";  // Plantilla login.html
    }
}
