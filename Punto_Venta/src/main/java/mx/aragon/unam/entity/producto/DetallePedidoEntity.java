package mx.aragon.unam.entity.producto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.aragon.unam.entity.provedor.PedidoProveedorEntity;

import java.math.BigDecimal;

@Entity(name = "detalle_pedidos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetallePedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private PedidoProveedorEntity pedido;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductoEntity producto;

    @Column(name = "cantidad_solicitada", nullable = false)
    private BigDecimal cantidadSolicitada;

    @Column(name = "cantidad_recibida")
    private BigDecimal cantidadRecibida;

    @Column(name = "precio_unitario", nullable = false)
    private BigDecimal precioUnitario;

    @Column(name = "subtotal", nullable = false)
    private BigDecimal subtotal;
}