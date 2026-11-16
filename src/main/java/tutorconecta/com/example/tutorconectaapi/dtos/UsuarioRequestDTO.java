package tutorconecta.com.example.tutorconectaapi.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioRequestDTO {
    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    @Email
    private String email;

    // Obligatorio al crear; al actualizar, si viene vacío se mantiene la contraseña actual
    private String passwordHash;

    private Boolean activo;
    private java.time.LocalDateTime fechaRegistro;

    @NotNull
    private Long idRol;
}
