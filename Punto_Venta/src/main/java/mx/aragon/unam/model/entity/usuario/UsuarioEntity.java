package mx.aragon.unam.model.entity.usuario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity(name = "usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "contrasena_hash", nullable = false, length = 255)
    private String contrasenaHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('ADMINISTRADOR', 'VENDEDOR', 'CLIENTE')")
    private TipoUsuario tipo = TipoUsuario.CLIENTE;

    @Column(name = "nombre_completo", length = 100, nullable = false)
    private String nombreCompleto;

    @Column(length = 15)
    private String telefono;

    @Column(columnDefinition = "TEXT")
    private String direccion;

    @Column(length = 20)
    private String rfc;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = false)
    private Boolean activo = true;

}