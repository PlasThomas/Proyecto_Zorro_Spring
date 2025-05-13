package mx.aragon.unam.service.provedor;

import mx.aragon.unam.model.entity.pedido.PedidoProveedorEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PedidoProveedorService {
    PedidoProveedorEntity save(PedidoProveedorEntity pedido);
    List<PedidoProveedorEntity> findAll();
    void deleteById(Integer id);
    PedidoProveedorEntity findById(Integer id);
}