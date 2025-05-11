package mx.aragon.unam.repository.usuario;


import mx.aragon.unam.model.entity.usuario.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, String> {
}