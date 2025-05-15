package mx.aragon.unam.service.producto.categorias;

import mx.aragon.unam.model.entity.producto.CategoriaEntity;
import mx.aragon.unam.repository.producto.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {
    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public CategoriaEntity save(CategoriaEntity producto) {
        return categoriaRepository.save(producto);
    }

    @Override
    public List<CategoriaEntity> findAll() {
        return categoriaRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        categoriaRepository.deleteById(id);
    }

    @Override
    public CategoriaEntity findById(Integer id) {
        return categoriaRepository.findById(id).orElse(null);
    }
}
