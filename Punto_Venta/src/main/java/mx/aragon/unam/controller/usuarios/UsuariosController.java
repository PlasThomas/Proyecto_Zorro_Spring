package mx.aragon.unam.controller.usuarios;

import jakarta.validation.Valid;
import mx.aragon.unam.model.entity.usuario.TipoUsuario;
import mx.aragon.unam.model.entity.usuario.UsuarioDTO;
import mx.aragon.unam.model.entity.usuario.UsuarioEntity;
import mx.aragon.unam.service.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "admin/usuarios")
public class UsuariosController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/")
    public String listaUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());
        return "vistas/admin/usuarios/lista-usuarios";
    }
    @GetMapping(value = "alta")
    public String altaUsuario(Model model) {
        UsuarioEntity usuario = new UsuarioEntity();
        model.addAttribute("contenido", "Registro de usuario");
        model.addAttribute("usuario", usuario);
        return "vistas/admin/usuarios/alta-usuarios";
    }

    @PostMapping(value = "alta")
    public String guardarUsuarios(@Valid @ModelAttribute("usuario") UsuarioEntity usuario,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        if (result.hasErrors() || usuario.getTipo() == TipoUsuario.ADMINISTRADOR || usuario.getTipo() == TipoUsuario.CLIENTE || usuario.getTipo() == null) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println("Error: " + error.getDefaultMessage());
            }
            model.addAttribute("mensajeError", "Fallo el registro");
            return "vistas/admin/usuarios/alta-usuarios";
        }

        if (usuario.getContrasenaHash() == null || usuario.getContrasenaHash().isEmpty()) {
            model.addAttribute("mensajeError", "La contraseña es requerida");
            return "vistas/admin/usuarios/alta-usuarios";
        }

        UsuarioEntity usuarioS = UsuarioEntity.builder()
                .email(usuario.getEmail())
                .contrasenaHash(passwordEncoder.encode(usuario.getContrasenaHash()))
                .tipo(usuario.getTipo())
                .nombreCompleto(usuario.getNombreCompleto())
                .telefono(usuario.getTelefono())
                .direccion(usuario.getDireccion())
                .rfc(usuario.getRfc())
                .build();
        usuarioService.save(usuarioS);
        redirectAttributes.addFlashAttribute("mensajeExito", "Cliente guardado correctamente");
        return "redirect:/admin/usuarios/";
    }

    @GetMapping(value = "modificar/{id}")
    public String modificarUsuario(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes,Model model) {
        UsuarioEntity usuario = usuarioService.findById(id);
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "usuario no encontrado");
            return "redirect:/admin/usuarios/"; // o muestra un error
        }
        model.addAttribute("contenido", "Modificar Usuario");
        model.addAttribute("usuario", usuario);
        return "vistas/admin/usuarios/alta-usuarios";
    }
    @GetMapping(value = "cambiar-estado/{id}")
    public String cambiarEstadoUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes,Model model) {
        UsuarioEntity usuario = usuarioService.findById(id);
        usuario.setActivo(!usuario.getActivo());
        usuarioService.save(usuario);
        redirectAttributes.addFlashAttribute("mensajeExito", "Estado cambiado correctamente");
        return "redirect:/admin/usuarios/";
    }

}
