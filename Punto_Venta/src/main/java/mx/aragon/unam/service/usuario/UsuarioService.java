package mx.aragon.unam.service.usuario;

import mx.aragon.unam.model.entity.usuario.UsuarioEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UsuarioService {
    UsuarioEntity save(UsuarioEntity usuario);
    List<UsuarioEntity> findAll();
    void deleteById(Integer id);
    UsuarioEntity findById(Integer id);
}