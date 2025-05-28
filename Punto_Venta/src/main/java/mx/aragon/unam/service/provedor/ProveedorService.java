package mx.aragon.unam.service.provedor;

import mx.aragon.unam.model.entity.proveedor.ProveedorEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ProveedorService {
    ProveedorEntity save(ProveedorEntity provedor);
    List<ProveedorEntity> findAll();
    void deleteById(Integer id);
    ProveedorEntity findById(Integer id);
}
