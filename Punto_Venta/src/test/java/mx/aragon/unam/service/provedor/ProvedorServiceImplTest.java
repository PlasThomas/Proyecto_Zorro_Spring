package mx.aragon.unam.service.provedor;

import mx.aragon.unam.model.entity.provedor.ProveedorEntity;
import mx.aragon.unam.repository.provedor.ProveedorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProvedorServiceImplTest {

    @Autowired
    private ProveedorService proveedorService;
    @Autowired
    private ProveedorRepository proveedorRepository;

    @Test
    void save() {
        ProveedorEntity proveedor = ProveedorEntity.builder()
                .nombreEmpresa("Siemens")
                .activo(true)
                .email("siemen@siemens.com")
                .contactoPrincipal("Telefono")
                .telefono("5555555555")
                .fechaRegistro(LocalDateTime.now())
                .build();
        System.out.println(proveedor);
        proveedorService.save(proveedor);
    }

    @Test
    void deleteById() {
        // Aqui se elimina la id seleccionada
        proveedorService.deleteById(11);
    }

    @Test
    void findById() {
        // Busasca
        ProveedorEntity found = proveedorService.findById(11);
        assertNotNull(found);
        System.out.println(found);
    }
    @Test
    void findAll() {
        List<ProveedorEntity> found = proveedorService.findAll();
        System.out.println(found);
    }

    // imprimer siempre lo que hay en la tabla
    @AfterEach
    void showAll() {
        System.out.println("=== Proveedores en BD ===");
        proveedorRepository.findAll().forEach(System.out::println);
    }
}