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

    @PostMapping("alta")
    public String guardarUsuarios(
            @Valid @ModelAttribute("usuario") UsuarioEntity usuario,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        boolean esNuevo = (usuario.getId() == null);

        // 1) Validaciones básicas
        if (result.hasErrors()
                || usuario.getTipo() == null
                || usuario.getTipo() == TipoUsuario.ADMINISTRADOR
                || usuario.getTipo() == TipoUsuario.CLIENTE) {
            model.addAttribute("mensajeError", "Datos inválidos o rol no permitido");
            return "vistas/admin/usuarios/alta-usuarios";
        }

        // 2) Validar email único
        if (esNuevo) {
            if (usuarioService.findByEmail(usuario.getEmail()).isPresent()) {
                model.addAttribute("mensajeError", "El email ya está registrado");
                return "vistas/admin/usuarios/alta-usuarios";
            }
        } else {
            UsuarioEntity existente = usuarioService.findById(usuario.getId());
            if (existente == null) {
                model.addAttribute("mensajeError", "El usuario a modificar no existe");
                return "vistas/admin/usuarios/alta-usuarios";
            }
            // Si cambió el email, verificar unicidad
            if (!existente.getEmail().equals(usuario.getEmail())
                    && usuarioService.findByEmail(usuario.getEmail()).isPresent()) {
                model.addAttribute("mensajeError", "El email ya está registrado por otro usuario");
                return "vistas/admin/usuarios/alta-usuarios";
            }
        }

        // 3) Gestionar contraseña
        if (esNuevo) {
            // en creación, es obligatorio enviar contraseña
            if (usuario.getContrasenaHash() == null || usuario.getContrasenaHash().isEmpty()) {
                model.addAttribute("mensajeError", "La contraseña es requerida");
                return "vistas/admin/usuarios/alta-usuarios";
            }
            usuario.setContrasenaHash(passwordEncoder.encode(usuario.getContrasenaHash()));
        } else {
            // en edición:
            if (usuario.getContrasenaHash() != null && !usuario.getContrasenaHash().isEmpty()) {
                // si envían nueva contraseña, la codificamos
                usuario.setContrasenaHash(passwordEncoder.encode(usuario.getContrasenaHash()));
            } else {
                // si no, conservamos la anterior
                UsuarioEntity existente = usuarioService.findById(usuario.getId());
                usuario.setContrasenaHash(existente.getContrasenaHash());
            }
        }

        // 4) Guardar (INSERT o UPDATE según traiga id o no)
        usuarioService.save(usuario);

        redirectAttributes.addFlashAttribute("mensajeExito",
                esNuevo ? "Usuario creado correctamente" : "Usuario modificado correctamente");
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
