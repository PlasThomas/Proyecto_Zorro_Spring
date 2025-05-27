package mx.aragon.unam.controller.ventas;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import mx.aragon.unam.model.entity.producto.ProductoEntity;
import mx.aragon.unam.model.entity.producto.ProductoSeleccionado;
import mx.aragon.unam.model.entity.usuario.TipoUsuario;
import mx.aragon.unam.model.entity.usuario.UsuarioDTO;
import mx.aragon.unam.model.entity.usuario.UsuarioEntity;
import mx.aragon.unam.model.entity.venta.DetalleVentaEntity;
import mx.aragon.unam.model.entity.venta.MetodoPago;
import mx.aragon.unam.model.entity.venta.VentaEntity;
import mx.aragon.unam.service.producto.categorias.CategoriaService;
import mx.aragon.unam.service.producto.productos.ProductoService;
import mx.aragon.unam.service.provedor.ProveedorService;
import mx.aragon.unam.service.usuario.UsuarioService;
import mx.aragon.unam.service.venta.detalleVenta.DetalleVentaService;
import mx.aragon.unam.service.venta.venta.VentaService;
import mx.aragon.unam.util.EmailService;
import mx.aragon.unam.util.PDFgenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    ProductoSeleccionado carrito = new ProductoSeleccionado();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @GetMapping(value = "inicio")
    public String vistaInicioCajere(Model model) {
        return "vistas/vendedor/inicio";
    }

    @GetMapping(value = "alta-cliente")
    public String altsCliente(Model model) {
        UsuarioEntity nuevoCliente = new UsuarioEntity();
        model.addAttribute("cliente", nuevoCliente);
        return "vistas/vendedor/alta-cliente";
    }

    @PostMapping(value = "alta-cliente")
    public String guardarCliente(@Valid @ModelAttribute("cliente") UsuarioDTO clienteDTO,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println("Error: " + error.getDefaultMessage());
            }
            model.addAttribute("mensajeError", "Fallo el registro");
            return "vistas/vendedor/alta-cliente";
        }
        UsuarioEntity cliente = UsuarioEntity.builder()
                .email(clienteDTO.getEmail())
                .contrasenaHash(passwordEncoder.encode(clienteDTO.getPassword()))
                .nombreCompleto(clienteDTO.getNombreCompleto())
                .telefono(clienteDTO.getTelefono())
                .direccion(clienteDTO.getDireccion())
                .rfc(clienteDTO.getRfc())
                .build();
        usuarioService.save(cliente);
        redirectAttributes.addFlashAttribute("mensajeExito", "Cliente guardado correctamente");
        return "redirect:/cajere/inicio";
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
                model.addAttribute("mensajeError", "El usuario seleccionado no es un cliente válido");
                return "vistas/vendedor/inicio";
            }
        } catch (Exception e) {
            model.addAttribute("mensajeError", "Error al seleccionar cliente: " + e.getMessage());
            return "vistas/vendedor/inicio";
        }
    }

    @GetMapping("venta/buscar-productos")
    public String buscarProductos(
            @RequestParam(required = false) Integer codigo,
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) Integer proveedorId,
            HttpSession session,
            Model model) {

        try {
            cargarDatosProductos(model);
            List<ProductoEntity> productosFiltrados = new ArrayList<>();
            if (codigo != null) {
                ProductoEntity producto = productoService.findById(codigo);
                if (producto != null) {
                    this.carrito.add(producto);
                }else{
                    model.addAttribute("mensajeError", "Producto no encontrado");
                }
                cargarDatosProductos(model);
                model.addAttribute("metodosPago", MetodoPago.values());
                model.addAttribute("compra", this.carrito);
                model.addAttribute("cliente", this.cliente);
                return "vistas/vendedor/inicio";
            }else {
                productosFiltrados = productoService.buscarPorCategoriaYProveedor(categoriaId,proveedorId);
                model.addAttribute("productos", productosFiltrados);
                cargarDatosProductos(model);
                model.addAttribute("cliente", this.cliente);
                return "vistas/vendedor/inicio";
            }
        } catch (Exception e) {
            model.addAttribute("mensajeError", "Error en búsqueda: " + e.getMessage());
            cargarDatosProductos(model);
            return "vistas/vendedor/inicio";
        }
    }

    @PostMapping("venta/agregar-producto/{id}")
    public String agregarProducto(@PathVariable("id") Integer id, Model model) {
        if(id != null){
            ProductoEntity producto = productoService.findById(id);
            this.carrito.add(producto);
        } else {
            model.addAttribute("mensajeError", "Error al agregar producto");
        }
        cargarDatosProductos(model);
        model.addAttribute("metodosPago", MetodoPago.values());
        model.addAttribute("compra", this.carrito);
        model.addAttribute("cliente", this.cliente);
        return "vistas/vendedor/inicio";
    }

    @PostMapping("venta/eliminar-producto/{id}")
    public String eliminarProducto(@PathVariable("id") Integer id, Model model) {
        if(id != null){
            ProductoEntity producto = productoService.findById(id);
            carrito.remove(producto);
        } else {
            model.addAttribute("mensajeError", "Error al agregar producto");
        }
        cargarDatosProductos(model);
        model.addAttribute("metodosPago", MetodoPago.values());
        model.addAttribute("compra", this.carrito);
        model.addAttribute("cliente", this.cliente);
        return "vistas/vendedor/inicio";
    }

    @GetMapping("venta/borrar-venta")
    public String borrarVenta(Model model) {
        this.carrito.clear();
        cargarDatosProductos(model);
        model.addAttribute("compra", this.carrito);
        model.addAttribute("cliente", this.cliente);
        return "vistas/vendedor/inicio";
    }

    @Transactional
    @PostMapping("venta/procesar-venta")
    public String procesarVenta(
            @RequestParam(required = false) String observaciones,
            @RequestParam MetodoPago metodoPago,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // Validaciones básicas
        if (carrito.getProductos().isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", "No hay productos en el carrito");
            return "redirect:/cajere/inicio";
        }
        if (cliente == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "Selecciona un cliente primero");
            return "redirect:/cajere/inicio";
        }

        // Validar stock antes de procesar
        for (ProductoSeleccionado.ProductosInter item : carrito.getProductos()) {
            ProductoEntity producto = productoService.findById(item.getProducto().getId());
            if (producto.getExistencia().compareTo(item.getCantidad()) < 0) {
                redirectAttributes.addFlashAttribute("mensajeError",
                        "Stock insuficiente para: " + producto.getNombre());
                return "redirect:/cajere/inicio";
            }
        }

        // Obtener el vendedor (usuario actual autenticado)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UsuarioEntity> vendedor = usuarioService.findByEmail(authentication.getName());

        // Crear y guardar la venta
        VentaEntity venta = VentaEntity.builder()
                .fechaVenta(LocalDateTime.now())
                .cliente(cliente)
                .vendedor(vendedor.get())
                .total(carrito.getPrecioTotal())
                .metodoPago(metodoPago)
                .build();
        venta = ventaService.save(venta);

        // Guardar detalles de venta y actualizar stock
        for (ProductoSeleccionado.ProductosInter item : carrito.getProductos()) {
            ProductoEntity producto = item.getProducto();

            // Actualizar stock
            BigDecimal nuevoStock = producto.getExistencia().subtract(item.getCantidad());
            producto.setExistencia(nuevoStock);
            productoService.save(producto); // Actualiza el producto en la base de datos

            // Crear detalle de venta
            DetalleVentaEntity detalle = DetalleVentaEntity.builder()
                    .venta(venta)
                    .producto(producto)
                    .cantidad(item.getCantidad())
                    .precioUnitario(producto.getPrecioVenta())
                    .totalLinea(producto.getPrecioVenta().multiply(item.getCantidad()))
                    .build();
            detalleVentaService.save(detalle);
        }

        // Limpiar carrito y cliente
        carrito.clear();
        cliente = null;
// 1. Obtener detalles de la venta
        List<DetalleVentaEntity> detalles = detalleVentaService.findByVenta(venta);

// 2. Generar el PDF
        String logoPath = "src/main/resources/static/logo.png";
        String pdfPath = "venta_" + venta.getIdVenta() + ".pdf";

        try {
            PDFgenerator.generarPDFVenta(venta, detalles, logoPath, pdfPath);

            // 3. Enviar el PDF por correo


            this.emailService.enviarConAdjunto(
                    "plascenciaramosjosemanuel@gmail.com", // Asegúrate de que Cliente tenga este campo
                    "Detalle de tu compra - ID: " + venta.getIdVenta(),
                    "Gracias por tu compra. Adjunto encontrarás el ticket.",
                    pdfPath
            );

            // 4. Borrar el PDF si deseas
            new File(pdfPath).delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
        redirectAttributes.addFlashAttribute("mensajeExito",
                "Venta registrada con éxito (ID: " + venta.getIdVenta() + ")");
        return "redirect:/cajere/inicio";
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
