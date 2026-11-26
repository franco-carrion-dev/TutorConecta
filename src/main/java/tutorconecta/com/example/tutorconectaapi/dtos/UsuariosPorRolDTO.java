package tutorconecta.com.example.tutorconectaapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuariosPorRolDTO {
    private Long idRol;
    private String denominacion;
    private Long totalUsuarios;
    private Long activos;
    private Long inactivos;
}
