package mx.aragon.unam.repository.historial;

import mx.aragon.unam.model.entity.historial.HistorialPedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialPedidoRepository extends JpaRepository<HistorialPedidoEntity, Integer> {
}
