package mx.aragon.unam.service.producto.productos;

import mx.aragon.unam.model.entity.producto.CategoriaEntity;
import mx.aragon.unam.model.entity.producto.ProductoEntity;
import mx.aragon.unam.model.entity.producto.UnidadMedida;
import mx.aragon.unam.model.entity.provedor.ProveedorEntity;
import mx.aragon.unam.repository.producto.ProductoRepository;
import mx.aragon.unam.repository.provedor.ProveedorRepository;
import mx.aragon.unam.service.producto.categorias.CategoriaService;
import mx.aragon.unam.service.provedor.ProveedorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
class ProductoServiceImplTest {
    @Autowired
    private ProductoService productoService;
    @Autowired
    private ProveedorService proveedorService;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private CategoriaService categoriaService;

    @Test
    void save() {
        // Arrange
        ProveedorEntity proveedor = proveedorService.findById(5);
        CategoriaEntity categoria = categoriaService.findById(4);

        ProductoEntity producto = ProductoEntity.builder()
                .nombre("Producto de prueba")
                .proveedor(proveedor)
                .categoria(categoria)
                .precioCompra(new BigDecimal("20.13"))
                .precioVenta(new BigDecimal("25.30"))
                .existencia(new BigDecimal("100"))
                .unidadMedida(UnidadMedida.KILO)
                .activo(true)
                .build();

        productoService.save(producto);
    }
    @Test
    void findAll() {
        List<ProductoEntity> found = productoService.findAll();
    }

    @Test
    void deleteById() {
        productoService.deleteById(21);
    }

    @Test
    void findById() {
    }
    @AfterEach
    void showAll() {
        System.out.println("=== Proveedores en BD ===");
        productoRepository.findAll().forEach(System.out::println);
    }
}