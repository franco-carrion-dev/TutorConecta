package tutorconecta.com.example.tutorconectaapi.services;

import tutorconecta.com.example.tutorconectaapi.dtos.PagoRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.PagoResponseDTO;

import java.util.List;

public interface PagoService {

    PagoResponseDTO crear(PagoRequestDTO request);

    List<PagoResponseDTO> listarTodos();

    PagoResponseDTO buscarPorId(Long id);

    PagoResponseDTO actualizar(Long id, PagoRequestDTO request);

    void eliminar(Long id);
    List<PagoResponseDTO> buscarPorEstado(String estado);

    List<PagoResponseDTO> buscarPorTutorYEstado(Long idTutor, String estado);

}
