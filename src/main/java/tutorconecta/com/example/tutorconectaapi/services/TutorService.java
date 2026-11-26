package tutorconecta.com.example.tutorconectaapi.services;

import java.math.BigDecimal;
import tutorconecta.com.example.tutorconectaapi.dtos.IngresosTutorDTO;

import tutorconecta.com.example.tutorconectaapi.dtos.TutorRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.TutorResponseDTO;

import java.util.List;

public interface TutorService {

    TutorResponseDTO crear(TutorRequestDTO request);

    List<TutorResponseDTO> listarTodos();

    TutorResponseDTO buscarPorId(Long id);

    TutorResponseDTO actualizar(Long id, TutorRequestDTO request);

    void eliminar(Long id);

List<IngresosTutorDTO> reporteIngresos();
    List<TutorResponseDTO> buscarPorRangoTarifa(BigDecimal min, BigDecimal max);
}
