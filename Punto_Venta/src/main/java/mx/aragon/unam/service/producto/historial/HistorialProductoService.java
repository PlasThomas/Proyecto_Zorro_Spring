package mx.aragon.unam.service.producto.historial;

import mx.aragon.unam.model.entity.producto.HistorialProductoEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HistorialProductoService {
    HistorialProductoEntity save(HistorialProductoEntity historial);
    List<HistorialProductoEntity> findAll();
    void deleteById(Long id);
    HistorialProductoEntity findById(Long id);
}