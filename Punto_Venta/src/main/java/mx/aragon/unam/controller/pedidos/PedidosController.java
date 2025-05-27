package mx.aragon.unam.controller.pedidos;

import jakarta.servlet.http.HttpSession;
import mx.aragon.unam.model.entity.pedido.DetallePedidoEntity;
import mx.aragon.unam.model.entity.pedido.EstadoPedido;
import mx.aragon.unam.model.entity.pedido.PedidoProveedorEntity;
import mx.aragon.unam.model.entity.producto.ProductoEntity;
import mx.aragon.unam.model.entity.producto.ProductoSeleccionado;
import mx.aragon.unam.model.entity.provedor.ProveedorEntity;
import mx.aragon.unam.model.entity.usuario.TipoUsuario;
import mx.aragon.unam.model.entity.usuario.UsuarioEntity;
import mx.aragon.unam.service.pedidos.detallePedido.DetallePedidoService;
import mx.aragon.unam.service.pedidos.pedido.PedidoProveedorService;
import mx.aragon.unam.service.producto.productos.ProductoService;
import mx.aragon.unam.service.provedor.ProveedorService;
import mx.aragon.unam.service.usuario.UsuarioService;
import mx.aragon.unam.util.EmailService;
import mx.aragon.unam.util.ExcelGenerator;
import mx.aragon.unam.util.PDFgenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PedidosController {
    @Autowired
    ProductoService productoService;
    @Autowired
    ProveedorService proveedorService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    PedidoProveedorService pedidoProveedorService;
    @Autowired
    DetallePedidoService detallePedidoService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PDFgenerator pdfGenerator;
    @Autowired
    private ExcelGenerator excelGenerator;

    private String correoPruebas = "plascenciaramosjosemanuel@gmail.com";
    ProveedorEntity proveedor = null;
    ProductoSeleccionado carrito = new ProductoSeleccionado();

    @RequestMapping(value = "/pedidos", method = RequestMethod.GET)
    public String redirecionPedidos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        // Obtener el primer authority
        String authority = authentication.getAuthorities().iterator().next().getAuthority();

        try {
            TipoUsuario autoridad = TipoUsuario.valueOf(authority);
            return switch (autoridad) {
                case ADMINISTRADOR -> "redirect:/admin/pedidos";
                case FINANZAS -> "redirect:/finanzas/pedidos";
                case VENDEDOR, CLIENTE -> "redirect:/logout";
                default -> "redirect:/login?error=unauthorized";
            };
        } catch (IllegalArgumentException e) {
            // Por si el authority no coincide con ningún TipoUsuario
            return "redirect:/login?error=invalid_role";
        }
    }

    @GetMapping(value = "/admin/pedidos")
    public String listaPedidos(Model model) {
        model.addAttribute("pedidos", pedidoProveedorService.findAll());
        return "vistas/admin/pedidos/lista-pedido";
    }

    @GetMapping(value = "/admin/pedidos/alta")
    public String altaPedidos(Model model) {
        model.addAttribute("proveedores", proveedorService.findAll());
        return "vistas/admin/pedidos/alta-pedido";
    }

    @PostMapping("/admin/pedidos/seleccionar-proveedor")
    public String confirmarCliente(@RequestParam("proveedorId") Integer proveedorId, Model model) {
        try {
            this.proveedor = proveedorService.findById(proveedorId);
            if (this.proveedor != null) {
                cargarDatosPedido(model);
                return "vistas/admin/pedidos/alta-pedido";
            } else {
                model.addAttribute("mensajeError", "Error al cargar el proveedor");
                return "vistas/admin/pedidos/alta-pedido";
            }
        } catch (Exception e) {
            model.addAttribute("mensajeError", "Error al seleccionar proveedor: " + e.getMessage());
            return "vistas/admin/pedidos/alta-pedido";
        }
    }

    @PostMapping("/admin/pedidos/agregar-producto/{id}")
    public String agregarProducto(@PathVariable("id") Integer id, Model model) {
        if(id != null){
            ProductoEntity producto = productoService.findById(id);
            this.carrito.add(producto);
        } else {
            model.addAttribute("mensajeError", "Error al agregar producto");
        }
        cargarDatosPedido(model);
        return "vistas/admin/pedidos/alta-pedido";
    }

    @PostMapping("/admin/pedidos/eliminar-producto/{id}")
    public String eliminarProducto(@PathVariable("id") Integer id, Model model) {
        if(id != null){
            ProductoEntity producto = productoService.findById(id);
            carrito.remove(producto);
        } else {
            model.addAttribute("mensajeError", "Error al agregar producto");
        }
        cargarDatosPedido(model);
        return "vistas/admin/pedidos/alta-pedido";
    }

    @GetMapping("/admin/pedidos/borrar-pedido")
    public String borrarVenta(Model model) {
        this.carrito.clear();
        cargarDatosPedido(model);
        return "vistas/admin/pedidos/alta-pedido";
    }

    @PostMapping("/admin/pedidos/procesar-pedido")
    @Transactional
    public String procesarPedido(
            @RequestParam(required = false) String observaciones,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // 1. Validaciones básicas
        if (carrito.getProductos().isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", "No hay productos en el pedido");
            return "redirect:/admin/pedidos/alta";
        }
        if (proveedor == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "Selecciona un proveedor primero");
            return "redirect:/admin/pedidos/alta";
        }

        // 2. Obtener usuario de registro (autenticado)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsuarioEntity usuarioRegistro = usuarioService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3. Crear y guardar el pedido
        PedidoProveedorEntity pedido = PedidoProveedorEntity.builder()
                .proveedor(proveedor)
                .fechaPedido(LocalDateTime.now())
                .fechaEntrega(null)           // se puede setear luego
                .estado(EstadoPedido.PENDIENTE)
                .total(carrito.getPrecioTotal()) // asume carrito suma precios de compra
                .usuarioRegistro(usuarioRegistro)
                .build();
        pedido = pedidoProveedorService.save(pedido);

        // 4. Guardar detalles del pedido
        List<DetallePedidoEntity> detalles = new ArrayList<>();
        for (ProductoSeleccionado.ProductosInter prod : carrito.getProductos()) {
            BigDecimal cantidad = carrito.getCantidad(prod.getProducto());
            BigDecimal subtotal = prod.getProducto().getPrecioCompra().multiply(cantidad);

            DetallePedidoEntity detalle = DetallePedidoEntity.builder()
                    .pedido(pedido)
                    .producto(prod.getProducto())
                    .cantidadSolicitada(cantidad)
                    .cantidadRecibida(BigDecimal.ZERO)
                    .precioUnitario(prod.getProducto().getPrecioCompra())
                    .totalLinea(subtotal)
                    .build();
            detallePedidoService.save(detalle);
            detalles.add(detalle);
        }

        // 5. Limpiar carrito y proveedor seleccionado
        carrito.clear();
        proveedor = null;

        // 6. Generar archivos
        String logoPath   = "src/main/resources/static/images/logo.png";
        String pdfPath    = "pedido_" + pedido.getId() + ".pdf";
        String excelPath  = "pedido_" + pedido.getId() + ".xlsx";

        try {
            // PDF para el proveedor
            PDFgenerator.generarPDFPedido(pedido, detalles, logoPath, pdfPath);

            // Excel para administración
            excelGenerator.generarExcelPedido(pedido, detalles, excelPath);

            // 7. Enviar correos (ambos a correo de pruebas)
            emailService.enviarConAdjunto(
                    correoPruebas,
                    "Nuevo pedido al proveedor — ID: " + pedido.getId(),
                    "Adjunto encontrarás el PDF con el pedido al proveedor.",
                    pdfPath
            );

            emailService.enviarExcelConAdjunto(
                    correoPruebas,
                    "Nuevo pedido generado — ID: " + pedido.getId(),
                    "Adjunto encontrarás el Excel para gestión interna del pedido.",
                    excelPath
            );

            // borrar archivos temporales (opcional)
            new File(pdfPath).delete();
            new File(excelPath).delete();

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("mensajeError", "Error al generar o enviar archivos: " + e.getMessage());
            return "redirect:/admin/pedidos/alta";
        }

        redirectAttributes.addFlashAttribute("mensajeExito",
                "Pedido creado con éxito (ID: " + pedido.getId() + "). Correos enviados.");

        return "redirect:/admin/pedidos";
    }


    private void cargarDatosPedido(Model model) {
        model.addAttribute("proveedores", proveedorService.findAll());
        model.addAttribute("proveedorSeleccionado", this.proveedor);
        model.addAttribute("compra", this.carrito);
        // Si no hay búsqueda, mostrar todos los productos
        if (!model.containsAttribute("productos")&& this.proveedor != null) {
            model.addAttribute("productos", productoService.findAllByProveedorId(proveedor.getId()));
        }
    }

}
