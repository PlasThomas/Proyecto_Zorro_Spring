package mx.aragon.unam.service.venta;

import mx.aragon.unam.model.entity.venta.DetalleVentaEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DetalleVentaService {
    DetalleVentaEntity save(DetalleVentaEntity detalleVenta);
    List<DetalleVentaEntity> findAll();
    void deleteById(Integer id);
    DetalleVentaEntity findById(Integer id);
}