package mx.aragon.unam.controller.proveedores;

import jakarta.validation.Valid;
import mx.aragon.unam.model.entity.proveedor.ProveedorEntity;
import mx.aragon.unam.service.provedor.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping(value = "admin/proveedores")
public class ProveedoresController {

    @Autowired
    ProveedorService proveedorService;

    @GetMapping(value = "/")
    public String listaProveedores(Model model) {
        model.addAttribute("proveedores", proveedorService.findAll());
        return "vistas/admin/proveedores/lista-proveedores";
    }

    @GetMapping(value = "alta")
    public String altaUsuario(Model model) {
        ProveedorEntity proveedor = new ProveedorEntity();
        model.addAttribute("contenido", "Registro de usuario");
        model.addAttribute("proveedor", proveedor);
        return "vistas/admin/proveedores/alta-proveedor";
    }

    @PostMapping("alta")
    public String guardarProveedor(@Valid @ModelAttribute(value = "proveedor") ProveedorEntity proveedor, BindingResult result, Model model) {
        System.out.println("ID recibido del formulario: " + proveedor.getId());
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println("Error: " + error.getDefaultMessage());
            }
            model.addAttribute("mensajeError", "Error al guardar el proveedor");
            model.addAttribute("contenido", "Error al guardar el proveedor");
            return "vistas/admin/proveedores/alta-proveedor";
        }

        if (proveedor.getId() != null) {
            ProveedorEntity existente = proveedorService.findById(proveedor.getId());
            if (existente != null) {
                existente.setNombreEmpresa(proveedor.getNombreEmpresa());
                existente.setContactoPrincipal(proveedor.getContactoPrincipal());
                existente.setTelefono(proveedor.getTelefono());
                existente.setEmail(proveedor.getEmail());
                existente.setActivo(proveedor.getActivo());
                proveedorService.save(existente);
            } else {
                model.addAttribute("mensajeError", "El proveedor a modificar no existe");
                return "vistas/admin/proveedores/alta-proveedor";
            }
        } else {
            proveedor.setFechaRegistro(LocalDateTime.now());
            proveedorService.save(proveedor);
        }

        model.addAttribute("mensajeExito", "Proveedor guardado con éxito");
        List<ProveedorEntity> proveedores = proveedorService.findAll();
        model.addAttribute("proveedores", proveedores);
        return "vistas/admin/proveedores/lista-proveedores";
    }
    @GetMapping(value = "modificar/{id}")
    public String modificarProveedor(@PathVariable("id") Integer id, Model model) {
        ProveedorEntity proveedor = proveedorService.findById(id);
        model.addAttribute("proveedor", proveedor);
        model.addAttribute("contenido", "Modificar Proveedor");
        return "vistas/admin/proveedores/alta-proveedor";
    }
    @GetMapping(value = "eliminar/{id}")
    public String eliminarProveedor(@PathVariable("id") Integer id, Model model) {
        proveedorService.deleteById(id);
        model.addAttribute("mensajeExito", "Proveedor eliminado con éxito");
        return "redirect:/admin/proveedores/";
    }

}