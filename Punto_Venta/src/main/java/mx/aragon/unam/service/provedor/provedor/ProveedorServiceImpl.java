package mx.aragon.unam.service.provedor.provedor;

import mx.aragon.unam.model.entity.provedor.ProveedorEntity;
import mx.aragon.unam.repository.provedor.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorServiceImpl implements ProveedorService {
    private final ProveedorRepository provedorRepository;

    @Autowired
    public ProveedorServiceImpl(ProveedorRepository provedorRepository) {
        this.provedorRepository = provedorRepository;
    }

    @Override
    public ProveedorEntity save(ProveedorEntity provedor) {
        return provedorRepository.save(provedor);
    }

    @Override
    public List<ProveedorEntity> findAll() {
        return provedorRepository.findAll();

    }

    @Override
    public void deleteById(Integer id) {
        provedorRepository.deleteById(id);
    }

    @Override
    public ProveedorEntity findById(Integer id) {
        return provedorRepository.findById(id).orElse(null);
    }
}
