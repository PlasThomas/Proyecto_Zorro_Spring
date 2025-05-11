package mx.aragon.unam.service.usuario;

import mx.aragon.unam.model.entity.usuario.UsuarioEntity;
import mx.aragon.unam.repository.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UsuarioEntity save(UsuarioEntity usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public List<UsuarioEntity> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public void deleteById(String email) {
        usuarioRepository.deleteById(email);
    }

    @Override
    public UsuarioEntity findById(String email) {
        return usuarioRepository.findById(email).orElse(null);
    }
}