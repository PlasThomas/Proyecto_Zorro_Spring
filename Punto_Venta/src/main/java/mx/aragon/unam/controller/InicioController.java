package mx.aragon.unam.controller;

import mx.aragon.unam.model.entity.usuario.TipoUsuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class InicioController {
    @RequestMapping(value = "/inicio", method = RequestMethod.GET)
    public String redirecionInicio() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        // Obtener el primer authority
        String authority = authentication.getAuthorities().iterator().next().getAuthority();

        try {
            TipoUsuario autoridad = TipoUsuario.valueOf(authority);
            return switch (autoridad) {
                case ADMINISTRADOR -> "redirect:/admin/inicio";
                case FINANZAS -> "redirect:/finanzas/inicio";
                case VENDEDOR -> "redirect:/cajere/inicio";
                case CLIENTE -> "redirect:/logout";
                default -> "redirect:/login?error=unauthorized";
            };
        } catch (IllegalArgumentException e) {
            // Por si el authority no coincide con ningún TipoUsuario
            return "redirect:/login?error=invalid_role";
        }
    }

    @RequestMapping(value = "/admin/inicio",method = RequestMethod.GET)
    public String vistaInicioAdmin() {
        return "vistas/admin/inicio";
    }

    @RequestMapping(value = "/finanzas/inicio",method = RequestMethod.GET)
    public String vistaInicioFinanzas() {
        return "vistas/finanzas/inicio";
    }
}
