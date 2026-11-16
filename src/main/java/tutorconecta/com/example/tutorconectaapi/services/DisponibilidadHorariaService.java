package tutorconecta.com.example.tutorconectaapi.services;

import org.springframework.data.repository.query.Param;
import tutorconecta.com.example.tutorconectaapi.dtos.DisponibilidadHorariaRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.DisponibilidadHorariaResponseDTO;
import tutorconecta.com.example.tutorconectaapi.entities.DisponibilidadHoraria;

import java.util.List;

public interface DisponibilidadHorariaService {

    DisponibilidadHorariaResponseDTO crear(DisponibilidadHorariaRequestDTO request);

    List<DisponibilidadHorariaResponseDTO> listarTodos();

    DisponibilidadHorariaResponseDTO buscarPorId(Long id);

    DisponibilidadHorariaResponseDTO actualizar(Long id, DisponibilidadHorariaRequestDTO request);

    void eliminar(Long id);

    List<DisponibilidadHoraria> findDisponibilidadByTutorId(Long id);

    List<DisponibilidadHoraria> findByDiaSemana(String diaSemana);
}
