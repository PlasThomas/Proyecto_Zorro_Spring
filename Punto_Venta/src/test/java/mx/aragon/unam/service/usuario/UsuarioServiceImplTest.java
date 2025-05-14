package mx.aragon.unam.service.usuario;

import mx.aragon.unam.model.entity.usuario.UsuarioEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UsuarioServiceImplTest {

    @Autowired
    private UsuarioService usuarioService;

    @Test
    void save() {
        UsuarioEntity usuario = UsuarioEntity.builder()
                .email("email@email.com")
                .contrasenaHash("contrasena")
                .nombreCompleto("nombre")
                .telefono("telefono")
                .direccion("direccion")
                .rfc("rfc")
                .build();
        System.out.println(usuario);
        usuarioService.save(usuario);
    }
}