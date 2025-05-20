package mx.aragon.unam.controller.ventas;

import mx.aragon.unam.model.entity.producto.ProductoEntity;
import mx.aragon.unam.model.entity.usuario.TipoUsuario;
import mx.aragon.unam.model.entity.usuario.UsuarioEntity;
import mx.aragon.unam.service.producto.categorias.CategoriaService;
import mx.aragon.unam.service.producto.productos.ProductoService;
import mx.aragon.unam.service.provedor.ProveedorService;
import mx.aragon.unam.service.usuario.UsuarioService;
import mx.aragon.unam.service.venta.detalleVenta.DetalleVentaService;
import mx.aragon.unam.service.venta.venta.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "cajere")
public class VendedorController {

    @Autowired
    ProductoService productoService;
    @Autowired
    CategoriaService categoriaService;
    @Autowired
    ProveedorService proveedorService;
    @Autowired
    VentaService ventaService;
    @Autowired
    DetalleVentaService detalleVentaService;
    @Autowired
    UsuarioService usuarioService;

    UsuarioEntity cliente = null;

    List<ProductoEntity> productos = null;

    @GetMapping(value = "inicio")
    public String vistaInicioCajere() {
        return "vistas/vendedor/inicio";
    }//mensajeError

    @GetMapping(value = "alta-cliente")
    public String altsCliente(Model model) {
        UsuarioEntity nuevoCliente = new UsuarioEntity();
        model.addAttribute("cliente", nuevoCliente);
        return "vistas/vendedor/alta-cliente";
    }

    @PostMapping(value = "alta-cliente")
    public String guardarCliente(@ModelAttribute("cliente") UsuarioEntity cliente, Model model ) {
        return "vistas/vendedor/inicio";
    }

    @PostMapping(value = "venta/busca-cliente")
    public String vistaBuscaCliente(@RequestParam("clienteId") Integer idCliente, Model model) {
        UsuarioEntity bcliente = usuarioService.findById(idCliente);
        if (bcliente != null && bcliente.getTipo() == TipoUsuario.CLIENTE) {
            model.addAttribute("cliente", bcliente);
        } else {
            model.addAttribute("ermensajeErrorror", "Cliente no encontrado");
        }
        return "vistas/vendedor/inicio";
    }

    @PostMapping("venta/confirmar-cliente")
    public String confirmarCliente(@RequestParam("clienteId") Integer clienteId, Model model) {
        try {
            this.cliente = usuarioService.findById(clienteId);
            if (this.cliente != null && this.cliente.getTipo() == TipoUsuario.CLIENTE) {
                cargarDatosProductos(model);
                model.addAttribute("cliente", this.cliente);
                return "vistas/vendedor/inicio";
            } else {
                model.addAttribute("error", "El usuario seleccionado no es un cliente válido");
                cargarDatosProductos(model); // Recargar datos necesarios
                return "vistas/vendedor/inicio";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al seleccionar cliente: " + e.getMessage());
            cargarDatosProductos(model); // Recargar datos necesarios
            return "vistas/vendedor/inicio";
        }
    }

    @GetMapping("venta/buscar-productos")
    public String buscarProductos(
            @RequestParam(required = false) Integer codigo,
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) Integer proveedorId,
            Model model) {

        try {
            cargarDatosProductos(model);
            List<ProductoEntity> productosFiltrados = new ArrayList<>();
            if (codigo != null) {
                ProductoEntity producto = productoService.findById(codigo);
                if (producto != null) {
                    productosFiltrados.add(producto);
                }
                model.addAttribute("productos", productosFiltrados);
                model.addAttribute("cliente", this.cliente);
                return "vistas/vendedor/inicio";
            }else {
                productosFiltrados = productoService.buscarPorCategoriaYProveedor(categoriaId,proveedorId);
                model.addAttribute("productos", productosFiltrados);
                model.addAttribute("cliente", this.cliente);
                return "vistas/vendedor/inicio";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error en búsqueda: " + e.getMessage());
            cargarDatosProductos(model);
            return "vistas/vendedor/inicio";
        }
    }

    private void cargarDatosProductos(Model model) {
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("proveedores", proveedorService.findAll());

        // Si no hay búsqueda, mostrar todos los productos
        if (!model.containsAttribute("productos")) {
            model.addAttribute("productos", productoService.findAll());
        }
    }



}
