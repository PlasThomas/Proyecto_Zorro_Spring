package mx.aragon.unam.model.entity.producto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")    @Column(name = "nombre", nullable = false, length = 50, unique = true)
    private String nombre;

    @NotNull(message = "El campo 'activo' es obligatorio")
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @NotBlank(message = "Los detalles son obligatorios")
    @Size(min = 5, max = 255, message = "Los detalles deben tener entre 5 y 255 caracteres")
    @Column(name = "detalles")
    private String detalles;
}