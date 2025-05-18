package mx.aragon.unam.service.historial.historialPedido;

import mx.aragon.unam.model.entity.historial.HistorialPedidoEntity;
import mx.aragon.unam.repository.historial.HistorialPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistorialPedidoServiceImpl implements HistorialPedidoService{

    private final HistorialPedidoRepository historialPedidoRepository;

    @Autowired
    public HistorialPedidoServiceImpl(HistorialPedidoRepository historialPedidoRepository) {
        this.historialPedidoRepository = historialPedidoRepository;
    }

    @Override
    public HistorialPedidoEntity save(HistorialPedidoEntity historial) {
        return historialPedidoRepository.save(historial);
    }

    @Override
    public List<HistorialPedidoEntity> findAll() {
        return historialPedidoRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        historialPedidoRepository.deleteById(id);
    }

    @Override
    public HistorialPedidoEntity findById(Integer id) {
        return historialPedidoRepository.findById(id).orElse(null);
    }
}
