package mx.aragon.unam.repository.producto;

import mx.aragon.unam.model.entity.producto.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Integer> {
}