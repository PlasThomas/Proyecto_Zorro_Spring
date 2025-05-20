package mx.aragon.unam.repository.producto;

import mx.aragon.unam.model.entity.producto.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Integer> {
    @Query("SELECT p FROM productos p WHERE " +
            "(:idCategoria IS NULL OR p.categoria.id = :idCategoria) AND " +
            "(:idProveedor IS NULL OR p.proveedor.id = :idProveedor)")
    List<ProductoEntity> buscarPorCategoriaYProveedor(
            @Param("idCategoria") Integer idCategoria,
            @   Param("idProveedor") Integer idProveedor);
}