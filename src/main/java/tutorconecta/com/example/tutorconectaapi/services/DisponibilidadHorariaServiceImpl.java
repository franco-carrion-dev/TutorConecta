package tutorconecta.com.example.tutorconectaapi.services;

import java.math.BigDecimal;
import tutorconecta.com.example.tutorconectaapi.dtos.HorasPorDiaDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.HorasPorTutorDTO;

import tutorconecta.com.example.tutorconectaapi.components.EntityMapper;
import tutorconecta.com.example.tutorconectaapi.dtos.DisponibilidadHorariaRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.DisponibilidadHorariaResponseDTO;
import tutorconecta.com.example.tutorconectaapi.entities.DisponibilidadHoraria;
import tutorconecta.com.example.tutorconectaapi.repositories.DisponibilidadHorariaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisponibilidadHorariaServiceImpl implements DisponibilidadHorariaService {
    private final DisponibilidadHorariaRepository repository;
    private final EntityMapper mapper;

    public DisponibilidadHorariaServiceImpl(DisponibilidadHorariaRepository repository, EntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public DisponibilidadHorariaResponseDTO crear(DisponibilidadHorariaRequestDTO request) {
        return mapper.toDisponibilidadResponseDTO(repository.save(mapper.toDisponibilidadEntity(request)));
    }

    @Override
    public List<DisponibilidadHorariaResponseDTO> listarTodos() {
        return repository.findAll().stream().map(mapper::toDisponibilidadResponseDTO).toList();
    }

    @Override
    public DisponibilidadHorariaResponseDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDisponibilidadResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No existe una disponibilidad horaria con ID: " + id));
    }

    @Override
    public DisponibilidadHorariaResponseDTO actualizar(Long id, DisponibilidadHorariaRequestDTO request) {
        buscarPorId(id);
        DisponibilidadHoraria disponibilidad = mapper.toDisponibilidadEntity(request);
        disponibilidad.setIdDisponibilidad(id);
        return mapper.toDisponibilidadResponseDTO(repository.save(disponibilidad));
    }

    @Override
    public void eliminar(Long id) {
        DisponibilidadHoraria disponibilidad = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No existe una disponibilidad horaria con ID: " + id));
        repository.delete(disponibilidad);
    }

    @Override
    public List<DisponibilidadHoraria> findDisponibilidadByTutorId(Long id) {
        return repository.findDisponibilidadByTutorId(id);
    }

    @Override
    public List<DisponibilidadHoraria> findByDiaSemana(String diaSemana) {
        return repository.findByDiaSemana(diaSemana);
    }

@Override
    public List<HorasPorDiaDTO> reporteHorasPorDia() {
        return repository.horasDisponiblesPorDia().stream()
                .map(f -> new HorasPorDiaDTO((String) f[0], ((Number) f[1]).longValue(), new BigDecimal(f[2].toString())))
                .toList();
    }

    @Override
    public List<HorasPorTutorDTO> reporteHorasPorTutor() {
        return repository.horasDisponiblesPorTutor().stream()
                .map(f -> new HorasPorTutorDTO(((Number) f[0]).longValue(), (String) f[1], ((Number) f[2]).longValue(), new BigDecimal(f[3].toString())))
                .toList();
    }
}
