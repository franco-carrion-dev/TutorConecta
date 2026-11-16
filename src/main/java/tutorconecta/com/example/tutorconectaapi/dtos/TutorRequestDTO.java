package tutorconecta.com.example.tutorconectaapi.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TutorRequestDTO {
    @NotNull
    private Long idUsuario;

    private String biografia;

    @NotNull
    private BigDecimal tarifaPorHora;

    private Boolean verificado;
    private BigDecimal calificacionPromedio;

    private java.util.List<Long> idMaterias;
}
