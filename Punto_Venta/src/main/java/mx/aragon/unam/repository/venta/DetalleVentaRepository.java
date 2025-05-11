package mx.aragon.unam.repository.venta;

import mx.aragon.unam.model.entity.venta.DetalleVentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVentaEntity, Integer> {
}