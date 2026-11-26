package tutorconecta.com.example.tutorconectaapi.services;

import java.math.BigDecimal;
import tutorconecta.com.example.tutorconectaapi.dtos.GastoEstudianteDTO;

import tutorconecta.com.example.tutorconectaapi.dtos.EstudianteRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.EstudianteResponseDTO;

import java.util.List;

public interface EstudianteService {

    EstudianteResponseDTO crear(EstudianteRequestDTO request);

    List<EstudianteResponseDTO> listarTodos();

    EstudianteResponseDTO buscarPorId(Long id);

    EstudianteResponseDTO actualizar(Long id, EstudianteRequestDTO request);

    void eliminar(Long id);

List<GastoEstudianteDTO> reporteGastos();
    List<EstudianteResponseDTO> buscarPorPresupuestoMinimo(BigDecimal monto);
}
