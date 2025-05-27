package mx.aragon.unam.controller;

import mx.aragon.unam.model.entity.producto.ProductoEntity;
import mx.aragon.unam.model.entity.usuario.TipoUsuario;
import mx.aragon.unam.service.producto.productos.ProductoService;
import mx.aragon.unam.service.venta.venta.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class InicioController {

    @Autowired
    private VentaService ventaService;
    @Autowired
    private ProductoService productoService;

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
    public String vistaInicioAdmin(Model model) {
        Long ventasHoy = ventaService.countVentasDelDia();
        Long totalHoy = ventaService.totalVentasDelDia();
        List<ProductoEntity> productos = productoService.findAllOrderByExistenciaAsc();
        model.addAttribute("ventasHoy", ventasHoy);
        model.addAttribute("totalHoy", totalHoy);
        model.addAttribute("pedidosPendientes", 0);
        model.addAttribute("productosOrdenadosPorStock", productos);
        return "vistas/admin/inicio";
    }

    @RequestMapping(value = "/finanzas/inicio",method = RequestMethod.GET)
    public String vistaInicioFinanzas() {
        return "vistas/finanzas/inicio";
    }
}
