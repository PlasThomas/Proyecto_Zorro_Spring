package mx.aragon.unam.service.venta.venta;

import mx.aragon.unam.model.entity.venta.VentaEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VentaService {
    VentaEntity save(VentaEntity venta);
    List<VentaEntity> findAll();
    void deleteById(Integer id);
    VentaEntity findById(Integer id);
    Long countVentasDelDia();
    Long totalVentasDelDia();
}