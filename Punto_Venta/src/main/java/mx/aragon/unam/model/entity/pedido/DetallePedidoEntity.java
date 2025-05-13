package mx.aragon.unam.model.entity.pedido;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.aragon.unam.model.entity.producto.ProductoEntity;

import java.math.BigDecimal;

@Entity(name = "detalle_pedido")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetallePedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer idDetalle;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private PedidoProveedorEntity pedido;  // Asume que ya tienes una entidad PedidoProveedor

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductoEntity producto;  // Asume que ya tienes una entidad Producto

    @Column(name = "cantidad_solicitada", nullable = false, precision = 10, scale = 3)
    private BigDecimal cantidadSolicitada;

    @Column(name = "cantidad_recibida", precision = 10, scale = 3)
    private BigDecimal cantidadRecibida = BigDecimal.ZERO;

    @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "total_linea", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalLinea;
}
