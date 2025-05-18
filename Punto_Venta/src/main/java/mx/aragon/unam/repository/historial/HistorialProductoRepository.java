package mx.aragon.unam.repository.historial;

import mx.aragon.unam.model.entity.historial.HistorialProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialProductoRepository extends JpaRepository<HistorialProductoEntity, Integer> {
}