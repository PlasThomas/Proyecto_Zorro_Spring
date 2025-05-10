package mx.aragon.unam.entity.usuario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity(name = "clientes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteEntity {
    @Id
    @Column(name = "email", length = 100)
    private String email;

    @OneToOne
    @MapsId
    @JoinColumn(name = "email")
    private UsuarioEntity usuario;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "rfc", length = 20)
    private String rfc;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
}