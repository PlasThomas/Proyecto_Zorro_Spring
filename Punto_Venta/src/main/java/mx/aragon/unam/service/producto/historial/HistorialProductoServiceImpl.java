package mx.aragon.unam.service.producto.historial;

import mx.aragon.unam.model.entity.historial.HistorialProductoEntity;
import mx.aragon.unam.repository.producto.HistorialProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistorialProductoServiceImpl implements HistorialProductoService {

    private final HistorialProductoRepository historialProductoRepository;

    @Autowired
    public HistorialProductoServiceImpl(HistorialProductoRepository historialProductoRepository) {
        this.historialProductoRepository = historialProductoRepository;
    }

    @Override
    public HistorialProductoEntity save(HistorialProductoEntity historial) {
        return historialProductoRepository.save(historial);
    }

    @Override
    public List<HistorialProductoEntity> findAll() {
        return historialProductoRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        historialProductoRepository.deleteById(id);
    }

    @Override
    public HistorialProductoEntity findById(Integer id) {
        return historialProductoRepository.findById(id).orElse(null);
    }
}