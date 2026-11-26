package tutorconecta.com.example.tutorconectaapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MontoPorMetodoDTO {
    private String metodoPago;
    private Long cantidad;
    private BigDecimal montoTotal;
}
