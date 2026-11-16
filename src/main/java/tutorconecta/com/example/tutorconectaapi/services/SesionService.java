package tutorconecta.com.example.tutorconectaapi.services;

import tutorconecta.com.example.tutorconectaapi.dtos.SesionRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.SesionResponseDTO;

import java.util.List;

public interface SesionService {

    SesionResponseDTO crear(SesionRequestDTO request);

    List<SesionResponseDTO> listarTodos();

    SesionResponseDTO buscarPorId(Long id);

    SesionResponseDTO actualizar(Long id, SesionRequestDTO request);

    void updateEstadoSesion(Long id, String estado);

    void eliminar(Long id);
}
