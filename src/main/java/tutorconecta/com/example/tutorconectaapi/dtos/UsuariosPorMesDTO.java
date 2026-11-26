package tutorconecta.com.example.tutorconectaapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuariosPorMesDTO {
    private Integer mes;
    private Long cantidad;
}
