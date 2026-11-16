package tutorconecta.com.example.tutorconectaapi.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RegistroRequestDTO {
    @NotBlank
    private String nombre;
    @NotBlank
    private String apellido;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 6)
    private String password;
    /** TUTOR o ESTUDIANTE */
    @NotBlank
    private String rol;
    // Solo tutor
    private String biografia;
    private BigDecimal tarifaPorHora;
    // Solo estudiante
    private BigDecimal presupuestoMaximo;
}
