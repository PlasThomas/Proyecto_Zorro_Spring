    package mx.aragon.unam.model.entity.producto;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import mx.aragon.unam.model.entity.provedor.ProveedorEntity;

    import java.math.BigDecimal;

    @Entity(name = "productos")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class ProductoEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_producto")
        private Integer id;

        @Column(name = "nombre", nullable = false, length = 100)
        private String nombre;

        @ManyToOne
        @JoinColumn(name = "id_categoria")
        private CategoriaEntity categoria;

        @ManyToOne
        @JoinColumn(name = "id_proveedor")
        private ProveedorEntity proveedor;

        @Column(name = "precio_compra", nullable = false)
        private BigDecimal precioCompra;

        @Column(name = "precio_venta", nullable = false)
        private BigDecimal precioVenta;

        @Column(name = "existencia", nullable = false)
        private BigDecimal existencia;

        @Enumerated(EnumType.STRING)
        @Column(name = "unidad_medida", nullable = false)
        private UnidadMedida unidadMedida;

        @Column(name = "activo", nullable = false)
        private Boolean activo;

        // Metodo para obtener la ruta de la imagen
        @Transient // Esta anotación indica que no es un campo persistente en la base de datos
        public String getImagenPath() {
            return "/product-images/" + this.id + ".png";
        }

    }