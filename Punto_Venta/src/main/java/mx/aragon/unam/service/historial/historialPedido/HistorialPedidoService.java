package mx.aragon.unam.service.historial.historialPedido;

import mx.aragon.unam.model.entity.historial.HistorialPedidoEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HistorialPedidoService {
    HistorialPedidoEntity save(HistorialPedidoEntity historial);
    List<HistorialPedidoEntity> findAll();
    void deleteById(Integer id);
    HistorialPedidoEntity findById(Integer id);
}
