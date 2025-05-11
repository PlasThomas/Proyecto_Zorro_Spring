package mx.aragon.unam.model.entity.producto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.aragon.unam.model.entity.usuario.UsuarioEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "historial_productos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistorialProductoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductoEntity producto;

    @Column(name = "fecha_movimiento", nullable = false)
    private LocalDateTime fechaMovimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false)
    private TipoCambio tipoMovimiento;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal cantidad;

    @Column(name = "existencia_actual", nullable = false, precision = 10, scale = 3)
    private BigDecimal existenciaActual;

    @Column(name = "id_referencia")
    private Integer idReferencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_referencia")
    private TipoReferencia tipoReferencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    @Column(columnDefinition = "TEXT")
    private String notas;

}