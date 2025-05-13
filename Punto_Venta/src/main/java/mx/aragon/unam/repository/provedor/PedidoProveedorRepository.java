package mx.aragon.unam.repository.provedor;

import mx.aragon.unam.model.entity.pedido.PedidoProveedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoProveedorRepository extends JpaRepository<PedidoProveedorEntity, Integer> {
}