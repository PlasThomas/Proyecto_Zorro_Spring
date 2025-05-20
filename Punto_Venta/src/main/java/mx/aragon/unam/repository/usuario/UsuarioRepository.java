package mx.aragon.unam.repository.usuario;


import mx.aragon.unam.model.entity.usuario.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {
    @Query("SELECT u FROM usuarios u WHERE u.email = :email")
    Optional<UsuarioEntity> findByEmail(@Param("email") String email);
}