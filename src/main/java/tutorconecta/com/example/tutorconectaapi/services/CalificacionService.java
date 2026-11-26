package tutorconecta.com.example.tutorconectaapi.services;

import tutorconecta.com.example.tutorconectaapi.dtos.PromedioTutorDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.CalificacionesPorPuntuacionDTO;

import tutorconecta.com.example.tutorconectaapi.dtos.CalificacionRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.CalificacionResponseDTO;

import java.util.List;

public interface CalificacionService {

    CalificacionResponseDTO crear(CalificacionRequestDTO request);

    List<CalificacionResponseDTO> listarTodos();

    CalificacionResponseDTO buscarPorId(Long id);

    CalificacionResponseDTO actualizar(Long id, CalificacionRequestDTO request);

    void eliminar(Long id);

List<PromedioTutorDTO> reportePromedioPorTutor();
    List<CalificacionesPorPuntuacionDTO> reportePorPuntuacion();
}
