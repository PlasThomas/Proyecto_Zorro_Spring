package mx.aragon.unam.repository.provedor;

import mx.aragon.unam.model.entity.proveedor.ProveedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepository extends JpaRepository<ProveedorEntity, Integer> {
}