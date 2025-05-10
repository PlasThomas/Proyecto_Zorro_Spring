package mx.aragon.unam.service.venta;

import mx.aragon.unam.entity.venta.DetalleVentaEntity;
import mx.aragon.unam.repository.venta.DetalleVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleVentaServiceImpl implements DetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;

    @Autowired
    public DetalleVentaServiceImpl(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Override
    public DetalleVentaEntity save(DetalleVentaEntity detalleVenta) {
        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    public List<DetalleVentaEntity> findAll() {
        return detalleVentaRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        detalleVentaRepository.deleteById(id);
    }

    @Override
    public DetalleVentaEntity findById(Integer id) {
        return detalleVentaRepository.findById(id).orElse(null);
    }
}