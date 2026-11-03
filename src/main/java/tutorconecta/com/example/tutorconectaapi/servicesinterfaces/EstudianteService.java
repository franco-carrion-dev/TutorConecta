package tutorconecta.com.example.tutorconectaapi.servicesinterfaces;

import tutorconecta.com.example.tutorconectaapi.dtos.EstudianteRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.EstudianteResponseDTO;

import java.util.List;

public interface EstudianteService {

    EstudianteResponseDTO crear(EstudianteRequestDTO request);

    List<EstudianteResponseDTO> listarTodos();

    EstudianteResponseDTO buscarPorId(Long id);

    EstudianteResponseDTO actualizar(Long id, EstudianteRequestDTO request);

    void eliminar(Long id);
}
