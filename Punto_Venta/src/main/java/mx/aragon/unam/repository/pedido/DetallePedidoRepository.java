package mx.aragon.unam.repository.pedido;

import mx.aragon.unam.model.entity.pedido.DetallePedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedidoEntity, Integer> {
}
