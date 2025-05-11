package mx.aragon.unam.model.entity.producto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity(name = "categorias")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Short id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "detalles")
    private String detalles;
}