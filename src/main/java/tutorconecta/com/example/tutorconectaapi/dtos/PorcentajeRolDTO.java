package tutorconecta.com.example.tutorconectaapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PorcentajeRolDTO {
    private String denominacion;
    private Long cantidad;
    private BigDecimal porcentaje;
}
