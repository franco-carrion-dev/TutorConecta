package tutorconecta.com.example.tutorconectaapi.services;

import tutorconecta.com.example.tutorconectaapi.dtos.TutorRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.TutorResponseDTO;

import java.util.List;

public interface TutorService {

    TutorResponseDTO crear(TutorRequestDTO request);

    List<TutorResponseDTO> listarTodos();

    TutorResponseDTO buscarPorId(Long id);

    TutorResponseDTO actualizar(Long id, TutorRequestDTO request);

    void eliminar(Long id);
}
