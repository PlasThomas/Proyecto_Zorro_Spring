package mx.aragon.unam.service.pedidos.detallePedido;

import mx.aragon.unam.model.entity.pedido.DetallePedidoEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DetallePedidoService {
    DetallePedidoEntity save(DetallePedidoEntity historial);
    List<DetallePedidoEntity> findAll();
    void deleteById(Integer id);
    DetallePedidoEntity findById(Integer id);
}
