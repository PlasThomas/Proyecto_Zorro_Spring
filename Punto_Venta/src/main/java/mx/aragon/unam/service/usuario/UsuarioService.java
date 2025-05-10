package mx.aragon.unam.service.usuario;

import mx.aragon.unam.entity.usuario.UsuarioEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UsuarioService {
    UsuarioEntity save(UsuarioEntity usuario);
    List<UsuarioEntity> findAll();
    void deleteById(String email);
    UsuarioEntity findById(String email);
}