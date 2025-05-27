package mx.aragon.unam.service.producto.productos;

import mx.aragon.unam.model.entity.producto.ProductoEntity;
import mx.aragon.unam.repository.producto.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public ProductoEntity save(ProductoEntity producto) {
        return productoRepository.save(producto);
    }

    @Override
    public List<ProductoEntity> findAll() {
        return productoRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        productoRepository.deleteById(id);
    }

    @Override
    public ProductoEntity findById(Integer id) {
        return productoRepository.findById(id).orElse(null);
    }

    @Override
    public List<ProductoEntity> buscarPorCategoriaYProveedor(Integer categoriaId, Integer proveedorId) {
        return productoRepository.buscarPorCategoriaYProveedor(categoriaId, proveedorId);
    }

    @Override
    public List<ProductoEntity> findAllOrderByExistenciaAsc() {
        return productoRepository.findAllOrderByExistenciaAsc();
    }

    @Override
    public List<ProductoEntity> findAllByProveedorId(Integer idProveedor) {
        return productoRepository.findAllByProveedorId(idProveedor);
    }

}