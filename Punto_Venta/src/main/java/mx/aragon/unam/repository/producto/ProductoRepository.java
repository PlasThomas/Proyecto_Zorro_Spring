package mx.aragon.unam.repository.producto;

import mx.aragon.unam.entity.producto.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Integer> {
}