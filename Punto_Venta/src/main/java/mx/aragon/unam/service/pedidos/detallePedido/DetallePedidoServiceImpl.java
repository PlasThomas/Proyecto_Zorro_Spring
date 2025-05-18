package mx.aragon.unam.service.pedidos.detallePedido;

import mx.aragon.unam.model.entity.pedido.DetallePedidoEntity;
import mx.aragon.unam.repository.pedido.DetallePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService{

    private final DetallePedidoRepository detallePedidoRepository;

    @Autowired
    public DetallePedidoServiceImpl(DetallePedidoRepository detallePedidoRepository) {
        this.detallePedidoRepository = detallePedidoRepository;
    }

    @Override
    public DetallePedidoEntity save(DetallePedidoEntity historial) {
        return detallePedidoRepository.save(historial);
    }

    @Override
    public List<DetallePedidoEntity> findAll() {
        return detallePedidoRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        detallePedidoRepository.deleteById(id);
    }

    @Override
    public DetallePedidoEntity findById(Integer id) {
        return detallePedidoRepository.findById(id).orElse(null);
    }
}
