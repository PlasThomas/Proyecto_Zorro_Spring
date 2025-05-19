package mx.aragon.unam.controller.productos;

import mx.aragon.unam.service.producto.categorias.CategoriaService;
import mx.aragon.unam.service.producto.productos.ProductoService;
import mx.aragon.unam.service.provedor.ProveedorService;
import mx.aragon.unam.service.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProductosController {
    @Autowired
    private ProductoService productoService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private ProveedorService proveedorService;
    @RequestMapping(value = "/admin/inicio",method = RequestMethod.GET)
    public String listarProductos(Model model) {
        return "vistas/adminfinan/inicio";
    }
}
