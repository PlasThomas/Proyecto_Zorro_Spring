package mx.aragon.unam.service.venta;

import mx.aragon.unam.model.entity.venta.VentaEntity;
import mx.aragon.unam.repository.venta.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;

    @Autowired
    public VentaServiceImpl(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    public VentaEntity save(VentaEntity venta) {
        return ventaRepository.save(venta);
    }

    @Override
    public List<VentaEntity> findAll() {
        return ventaRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        ventaRepository.deleteById(id);
    }

    @Override
    public VentaEntity findById(Integer id) {
        return ventaRepository.findById(id).orElse(null);
    }
}