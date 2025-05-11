package mx.aragon.unam.model.entity.venta;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.aragon.unam.model.entity.producto.ProductoEntity;

import java.math.BigDecimal;

@Entity(name = "detalle_venta")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetalleVentaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_venta", nullable = false)
    private VentaEntity venta;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private ProductoEntity producto;

    @Column(name = "cantidad", nullable = false)
    private BigDecimal cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private BigDecimal precioUnitario;

    @Column(name = "total_linea", nullable = false)
    private BigDecimal totalLinea;
}