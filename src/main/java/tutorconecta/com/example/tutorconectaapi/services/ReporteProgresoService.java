package tutorconecta.com.example.tutorconectaapi.services;

import org.springframework.data.repository.query.Param;
import tutorconecta.com.example.tutorconectaapi.dtos.ReporteProgresoRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.ReporteProgresoResponseDTO;
import tutorconecta.com.example.tutorconectaapi.entities.ReporteProgreso;

import java.util.List;

public interface ReporteProgresoService {

    ReporteProgresoResponseDTO crear(ReporteProgresoRequestDTO request);

    List<ReporteProgresoResponseDTO> listarTodos();

    ReporteProgresoResponseDTO buscarPorId(Long id);

    ReporteProgresoResponseDTO actualizar(Long id, ReporteProgresoRequestDTO request);

    void eliminar(Long id);

    List<ReporteProgreso> findReportesByEstudianteId(Long idEstudiante);


    List<ReporteProgreso> findReportesByTutorId(Long idTutor);

}
