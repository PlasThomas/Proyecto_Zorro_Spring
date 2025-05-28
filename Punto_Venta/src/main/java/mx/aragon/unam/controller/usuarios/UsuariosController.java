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
        UsuarioDTO usuario = new UsuarioDTO();
        model.addAttribute("usuario", usuario);
        return "vistas/admin/usuarios/alta-usuarios";
    }

    @PostMapping(value = "alta")
    public String guardarUsuarios(@Valid @ModelAttribute("usuario") UsuarioDTO usuarioDTO,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        if (result.hasErrors() || usuarioDTO.getTipo() == TipoUsuario.ADMINISTRADOR || usuarioDTO.getTipo() == TipoUsuario.CLIENTE || usuarioDTO.getTipo() == null) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println("Error: " + error.getDefaultMessage());
            }
            model.addAttribute("mensajeError", "Fallo el registro");
            return "vistas/admin/usuarios/alta-usuarios";
        }
        System.out.println("Tipo recibido: " + usuarioDTO.getTipo());
        UsuarioEntity usuario = UsuarioEntity.builder()
                .email(usuarioDTO.getEmail())
                .contrasenaHash(passwordEncoder.encode(usuarioDTO.getPassword()))
                .tipo(usuarioDTO.getTipo())
                .nombreCompleto(usuarioDTO.getNombreCompleto())
                .telefono(usuarioDTO.getTelefono())
                .direccion(usuarioDTO.getDireccion())
                .rfc(usuarioDTO.getRfc())
                .build();
        usuarioService.save(usuario);
        redirectAttributes.addFlashAttribute("mensajeExito", "Cliente guardado correctamente");
        return "redirect:/admin/usuarios/";
    }

    @GetMapping(value = "modificar")
    public String modificarUsuario(Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());
        return "vistas/admin/usuarios/alta-usuarios";
    }
    @GetMapping(value = "cambiar-estado")
    public String cambiarEstadoUser(Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());
        return "vistas/admin/usuarios/lista-usuarios";
    }

}
