package mx.aragon.unam.repository.venta;

import mx.aragon.unam.model.entity.venta.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends JpaRepository<VentaEntity, Integer> {
}