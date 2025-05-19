package mx.aragon.unam.service.producto.categorias;

import mx.aragon.unam.model.entity.producto.CategoriaEntity;

import java.util.List;

public interface CategoriaService {
    CategoriaEntity save(CategoriaEntity categoria);
    List<CategoriaEntity> findAll();
    void deleteById(Short id);
    CategoriaEntity findById(Short id);
}
