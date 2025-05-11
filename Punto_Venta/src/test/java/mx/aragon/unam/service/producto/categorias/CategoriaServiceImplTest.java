package mx.aragon.unam.service.producto.categorias;

import mx.aragon.unam.model.entity.producto.CategoriaEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CategoriaServiceImplTest {
    @Autowired
    CategoriaService categoriaService;

    @Test
    void    save(){
        CategoriaEntity actor=CategoriaEntity.builder()
                .nombre("dorgas")
                .activo(true)
                .detalles("nose")
                .build();
        System.out.println(actor);
        categoriaService.save(actor);
    }

}