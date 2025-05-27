package mx.aragon.unam.service.venta.detalleVenta;

import mx.aragon.unam.model.entity.venta.DetalleVentaEntity;
import mx.aragon.unam.model.entity.venta.VentaEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DetalleVentaService {
    DetalleVentaEntity save(DetalleVentaEntity detalleVenta);
    List<DetalleVentaEntity> findAll();
    void deleteById(Integer id);
    DetalleVentaEntity findById(Integer id);
    List<DetalleVentaEntity> findByVenta(VentaEntity venta);
}