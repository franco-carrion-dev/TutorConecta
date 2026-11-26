package tutorconecta.com.example.tutorconectaapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MateriaCantidadDTO {
    private Long idMateria;
    private String nombre;
    private String nivelEducativo;
    private Long cantidad;
}
