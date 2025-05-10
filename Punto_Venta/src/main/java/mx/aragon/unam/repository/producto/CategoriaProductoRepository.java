package mx.aragon.unam.repository.producto;

import mx.aragon.unam.entity.producto.CategoriaProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaProductoRepository extends JpaRepository<CategoriaProductoEntity, Short> {
}