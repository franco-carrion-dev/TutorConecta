package tutorconecta.com.example.tutorconectaapi.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "tutores")
public class Tutor {
    @Id
    private Long idTutor; // FK que también es PK

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_tutor")
    private Usuario usuario;

    @Column(columnDefinition = "TEXT")
    private String biografia;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifaPorHora;

    private Boolean verificado;

    @Column(precision = 3, scale = 2)
    private BigDecimal calificacionPromedio;

    // Materias que dicta el tutor (tabla puente tutor_materias)
    @ManyToMany
    @JoinTable(name = "tutor_materias",
            joinColumns = @JoinColumn(name = "id_tutor"),
            inverseJoinColumns = @JoinColumn(name = "id_materia"))
    @lombok.EqualsAndHashCode.Exclude
    @lombok.ToString.Exclude
    private Set<Materia> materias = new HashSet<>();
}
