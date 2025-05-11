package mx.aragon.unam.model.entity.venta;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.aragon.unam.model.entity.usuario.UsuarioEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "ventas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VentaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Integer idVenta;

    @Column(name = "fecha_venta", nullable = false)
    private LocalDateTime fechaVenta;

    @ManyToOne
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_usuario", foreignKey = @ForeignKey(name = "fk_venta_cliente"))
    private UsuarioEntity cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_vendedor", referencedColumnName = "id_usuario", nullable = false, foreignKey = @ForeignKey(name = "fk_venta_vendedor"))
    private UsuarioEntity vendedor;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    private MetodoPago metodoPago;

}