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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductosController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProveedorService proveedorService;

    // Ruta donde se guardarán las imágenes
    private static final String IMAGES_DIR = "src/main/resources/static/product-images/";

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

    // Lista de productos para admin
    @GetMapping
    public String listaProductosAdmin(Model model) {
        List<ProductoEntity> productos = productoService.findAll();
        model.addAttribute("productos", productos);
        return "vistas/admin/productos/lista-productos";
    }

    // Mostrar formulario de nuevo producto
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new ProductoEntity());
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("proveedores", proveedorService.findAll());
        return "vistas/admin/productos/nuevo-producto";
    }

    // Guardar nuevo producto
    @PostMapping("/guardar")
    public String guardarProducto(
            @ModelAttribute("producto") ProductoEntity producto,
            BindingResult result,
            @RequestParam("imagen") MultipartFile imagen,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "vistas/admin/productos/nuevo-producto";
        }

        ProductoEntity productoGuardado = productoService.save(producto);

        if (!imagen.isEmpty()) {
            try {
                byte[] bytes = imagen.getBytes();
                Path path = Paths.get(IMAGES_DIR + productoGuardado.getId() + ".png");
                Files.write(path, bytes);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Error al guardar la imagen del producto");
                e.printStackTrace();
            }
        }

        redirectAttributes.addFlashAttribute("success", "Producto guardado correctamente");
        return "redirect:/productos";
    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Integer id, Model model) {
        ProductoEntity producto = productoService.findById(id);
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("proveedores", proveedorService.findAll());

        // Agregar la ruta de la imagen actual
        Path imagePath = Paths.get(IMAGES_DIR + id + ".png");
        if (Files.exists(imagePath)) {
            model.addAttribute("imagenActual", "/product-images/" + id + ".png");
        }

        return "vistas/admin/productos/editar-producto";
    }

    // Procesar formulario de edición
    @PostMapping("/editar/{id}")
    public String actualizarProducto(
            @PathVariable Integer id,
            @ModelAttribute("producto") ProductoEntity producto,
            BindingResult result,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "vistas/admin/productos/editar-producto";
        }

        // Guardar el producto
        producto.setId(id);
        ProductoEntity productoActualizado = productoService.save(producto);

        // Guardar la imagen solo si se proporcionó una nueva
        if (imagen != null && !imagen.isEmpty()) {
            try {
                byte[] bytes = imagen.getBytes();
                Path path = Paths.get(IMAGES_DIR + productoActualizado.getId() + ".png");
                Files.write(path, bytes);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Error al guardar la imagen del producto");
                e.printStackTrace();
            }
        }

        redirectAttributes.addFlashAttribute("success", "Producto actualizado correctamente");
        return "redirect:/productos";
    }

    // Endpoint para finanzas
    @RequestMapping(value = "/finanzas/productos", method = RequestMethod.GET)
    public String listaProductosFinanzas(Model model) {
        List<ProductoEntity> productos = productoService.findAll();
        model.addAttribute("productos", productos);
        return "vistas/finanzas/lista-productos";
    }
}