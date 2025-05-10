package mx.aragon.unam.entity.producto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.aragon.unam.entity.usuario.UsuarioEntity;

import java.time.LocalDateTime;

@Entity(name = "historial_productos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistorialProductoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductoEntity producto;

    @Column(name = "fecha_cambio", nullable = false)
    private LocalDateTime fechaCambio;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cambio", nullable = false)
    private TipoCambio tipoCambio;

    @ManyToOne
    @JoinColumn(name = "usuario_email", nullable = false)
    private UsuarioEntity usuario;

    @Column(name = "campo_afectado", length = 30)
    private String campoAfectado;

    @Column(name = "valor_anterior")
    private String valorAnterior;

    @Column(name = "valor_nuevo")
    private String valorNuevo;

    public enum TipoCambio {
        CREACION, MODIFICACION, AJUSTE_INVENTARIO, CAMBIO_PRECIO, DESACTIVACION
    }
}