package mx.aragon.unam.model.entity.historial;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.aragon.unam.model.entity.pedido.EstadoPedido;
import mx.aragon.unam.model.entity.pedido.PedidoProveedorEntity;
import mx.aragon.unam.model.entity.usuario.UsuarioEntity;

import java.time.LocalDateTime;

@Entity(name = "historial_pedidos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistorialPedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Long idMovimiento;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private PedidoProveedorEntity pedido;

    @Column(name = "fecha_movimiento", nullable = false, updatable = false)
    private LocalDateTime fechaMovimiento = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false)
    private TipoMovimiento tipoMovimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_anterior")
    private EstadoPedido estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_nuevo")
    private EstadoPedido estadoNuevo;

    @Column(name = "campo_modificado", length = 50)
    private String campoModificado;

    @Column(name = "valor_anterior", columnDefinition = "TEXT")
    private String valorAnterior;

    @Column(name = "valor_nuevo", columnDefinition = "TEXT")
    private String valorNuevo;

    @Column(name = "id_referencia")
    private Integer idReferencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_referencia")
    private TipoReferenciaPedido tipoReferencia;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;
}
