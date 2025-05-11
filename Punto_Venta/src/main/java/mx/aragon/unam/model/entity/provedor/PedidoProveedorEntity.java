package mx.aragon.unam.model.entity.provedor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.aragon.unam.model.entity.usuario.UsuarioEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "pedidos_proveedor")
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

    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "id_usuario_registro", nullable = false)
    private UsuarioEntity usuarioRegistro;

}