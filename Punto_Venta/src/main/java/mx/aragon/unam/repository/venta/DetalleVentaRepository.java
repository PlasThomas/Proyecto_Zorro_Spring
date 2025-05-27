package mx.aragon.unam.repository.venta;

import mx.aragon.unam.model.entity.venta.DetalleVentaEntity;
import mx.aragon.unam.model.entity.venta.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVentaEntity, Integer> {
    List<DetalleVentaEntity> findByVenta(VentaEntity venta);
}