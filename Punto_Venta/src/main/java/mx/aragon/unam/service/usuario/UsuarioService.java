package mx.aragon.unam.service.usuario;

import mx.aragon.unam.model.entity.usuario.UsuarioEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UsuarioService {
    UsuarioEntity save(UsuarioEntity usuario);
    List<UsuarioEntity> findAll();
    void deleteById(Integer id);
    UsuarioEntity findById(Integer id);
    Optional<UsuarioEntity> findByEmail(String email);
}