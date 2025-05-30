package mx.aragon.unam.controller.ventas;

import mx.aragon.unam.model.entity.usuario.TipoUsuario;
import mx.aragon.unam.service.venta.detalleVenta.DetalleVentaService;
import mx.aragon.unam.service.venta.venta.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class VentasController {
    @Autowired
    VentaService ventaService;
    @Autowired
    DetalleVentaService detalleVentaService;

    @RequestMapping(value = "/ventas", method = RequestMethod.GET)
    public String redirecionarVentas(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        // Obtener el primer authority
        String authority = authentication.getAuthorities().iterator().next().getAuthority();

        try {
            TipoUsuario autoridad = TipoUsuario.valueOf(authority);
            return switch (autoridad) {
                case ADMINISTRADOR -> "redirect:/admin/ventas/";
                case FINANZAS -> "redirect:/finanzas/ventas";
                case VENDEDOR, CLIENTE -> "redirect:/logout";
                default -> "redirect:/login?error=unauthorized";
            };
        } catch (IllegalArgumentException e) {
            // Por si el authority no coincide con ningún TipoUsuario
            return "redirect:/login?error=invalid_role";
        }
    }

    @GetMapping(value = "/admin/ventas/")
    public String listaVentas(Model model) {
        model.addAttribute("ventas", ventaService.findAll());
        return "vistas/admin/ventas/lista-ventas";
    }

    @GetMapping(value = "/admin/ventas/detalles/{id}")
    public String listaDetalleVenta(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("detallesVenta", detalleVentaService.findByVenta(ventaService.findById(id)));
        return "vistas/admin/ventas/lista-detallesVenta";
    }

}
