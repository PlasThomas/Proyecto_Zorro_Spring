package mx.aragon.unam.controller.productos;

import mx.aragon.unam.model.entity.producto.ProductoEntity;
import mx.aragon.unam.model.entity.usuario.TipoUsuario;
import mx.aragon.unam.service.producto.categorias.CategoriaService;
import mx.aragon.unam.service.producto.productos.ProductoService;
import mx.aragon.unam.service.provedor.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class ProductosController {
    @Autowired
    private ProductoService productoService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private ProveedorService proveedorService;

    // Redireccion dependiendo del tipo de usuario
    @RequestMapping(value = "/productos", method = RequestMethod.GET)
    public String redirecionProductos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        String authority = authentication.getAuthorities().iterator().next().getAuthority();
        try {
            TipoUsuario autoridad = TipoUsuario.valueOf(authority);
            return switch (autoridad) {
                case ADMINISTRADOR -> "redirect:/admin/productos";
                case FINANZAS -> "redirect:/finanzas/productos";
                case VENDEDOR -> "redirect:/cajere/productos";
                default -> "redirect:/login?error=unauthorized";
            };
        } catch (IllegalArgumentException e) {
            return "redirect:/login?error=invalid_role";
        }
    }

//    <=================================================================================================================>
//      END POINTS ADMIN

    @RequestMapping(value = "/admin/productos",method = RequestMethod.GET)
    public String listaProductosAdmin(Model model) {
        List<ProductoEntity> productos = productoService.findAll();
        model.addAttribute("productos", productos);
        return "vistas/admin/productos/lista-productos";
    }

    @RequestMapping(value = "/admin/alta-productos",method = RequestMethod.GET)
    public String altaProductos(Model model) {
        List<ProductoEntity> productos = productoService.findAll();
        model.addAttribute("productos", productos);
        return "vistas/productos/productos";
    }

    @RequestMapping(value = "/admin/alta-productos",method = RequestMethod.POST)
    public String guardarProducto(Model model) {
        List<ProductoEntity> productos = productoService.findAll();
        model.addAttribute("productos", productos);
        return "vistas/productos/productos";
    }

    @RequestMapping(value = "/admin/modificar-productos/{id}",method = RequestMethod.GET)
    public String modificarProductos(@PathVariable("id") Integer id, Model model) {
        List<ProductoEntity> productos = productoService.findAll();
        model.addAttribute("productos", productos);
        return "vistas/productos/productos";
    }

    @RequestMapping(value = "/admin/eliminar-productos/{id}",method = RequestMethod.GET)
    public String bajaProductos(@PathVariable("id") Integer id, Model model) {
        productoService.deleteById(id);
        return "redirect:/admin/productos";
    }

//    <=================================================================================================================>
//      END POINTS FINANZAS
    @RequestMapping(value = "/finanzas/productos",method = RequestMethod.GET)
    public String listaProductosFinanzas(Model model) {
        List<ProductoEntity> productos = productoService.findAll();
        model.addAttribute("productos", productos);
        return "vistas/finanzas/lista-productos";
    }

//    <=================================================================================================================>
//      END POINTS CAJERE

    @RequestMapping(value = "/cajere/productos",method = RequestMethod.GET)
    public String listaProductosCajere(Model model) {
        List<ProductoEntity> productos = productoService.findAll();
        model.addAttribute("productos", productos);
        return "vistas/vendedor/lista-productos";
    }
}
