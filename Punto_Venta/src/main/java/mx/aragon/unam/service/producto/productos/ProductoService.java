package mx.aragon.unam.service.producto.productos;

import jakarta.persistence.criteria.CriteriaBuilder;
import mx.aragon.unam.model.entity.producto.ProductoEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductoService {
    ProductoEntity save(ProductoEntity producto);
    List<ProductoEntity> findAll();
    void deleteById(Integer id);
    ProductoEntity findById(Integer id);
    List<ProductoEntity> buscarPorCategoriaYProveedor(Integer categoriaId, Integer proveedorId);
}