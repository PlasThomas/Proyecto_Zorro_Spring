package mx.aragon.unam.repository.venta;

import mx.aragon.unam.model.entity.venta.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends JpaRepository<VentaEntity, Integer> {
    // Consulta para obtener el total de ventas del día actual
    @Query("SELECT COUNT(v) FROM ventas v WHERE DATE(v.fechaVenta) = CURRENT_DATE")
    Long countVentasDelDia();
    @Query("SELECT SUM(v.total) FROM ventas v WHERE DATE(v.fechaVenta) = CURRENT_DATE")
    Long totalVentasDelDia();
}