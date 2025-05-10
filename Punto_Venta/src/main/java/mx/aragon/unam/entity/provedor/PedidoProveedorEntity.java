package mx.aragon.unam.entity.provedor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.aragon.unam.entity.producto.DetallePedidoEntity;
import mx.aragon.unam.entity.usuario.UsuarioEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "pedidos_proveedores")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PedidoProveedorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_proveedor", nullable = false)
    private ProveedorEntity proveedor;

    @Column(name = "fecha_pedido", nullable = false)
    private LocalDateTime fechaPedido;

    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoPedido estado;

    @Column(name = "subtotal", nullable = false)
    private BigDecimal subtotal;

    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @Column(name = "notas")
    private String notas;

    @ManyToOne
    @JoinColumn(name = "usuario_registro", nullable = false)
    private UsuarioEntity usuarioRegistro;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<DetallePedidoEntity> detalles;

    public enum EstadoPedido {
        PENDIENTE, COMPLETADO, CANCELADO
    }
}