package tutorconecta.com.example.tutorconectaapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromedioTutorDTO {
    private Long idTutor;
    private String nombreTutor;
    private Long cantidad;
    private BigDecimal promedio;
}
