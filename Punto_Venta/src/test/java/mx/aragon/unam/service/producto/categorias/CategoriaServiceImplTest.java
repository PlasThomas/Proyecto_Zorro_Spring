package mx.aragon.unam.service.producto.categorias;

import mx.aragon.unam.model.entity.producto.CategoriaEntity;
import mx.aragon.unam.repository.producto.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CategoriaServiceImplTest {
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void save() {
        CategoriaEntity categorias = CategoriaEntity.builder()
                .nombre("Nombre  pruebas ")
                .activo(true)
                .detalles("Datos de prueba")
                .build();
        categoriaRepository.save(categorias);
        System.out.println(categorias);
    }

    @Test
    void findAll() {
        List<CategoriaEntity> categorias = categoriaService.findAll();
        System.out.println(categorias);
    }

    @Test
    void deleteById() {
        categoriaService.deleteById(11);
    }

    @Test
    void findById() {
        CategoriaEntity categoria = categoriaService.findById(8);
        System.out.println(categoria);
    }
}