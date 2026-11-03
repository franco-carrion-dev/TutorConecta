package tutorconecta.com.example.tutorconectaapi.servicesinterfaces;

import tutorconecta.com.example.tutorconectaapi.dtos.ReporteProgresoRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.ReporteProgresoResponseDTO;

import java.util.List;

public interface ReporteProgresoService {

    ReporteProgresoResponseDTO crear(ReporteProgresoRequestDTO request);

    List<ReporteProgresoResponseDTO> listarTodos();

    ReporteProgresoResponseDTO buscarPorId(Long id);

    ReporteProgresoResponseDTO actualizar(Long id, ReporteProgresoRequestDTO request);

    void eliminar(Long id);
}
