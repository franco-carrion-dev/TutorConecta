package tutorconecta.com.example.tutorconectaapi.services;

import java.math.BigDecimal;
import tutorconecta.com.example.tutorconectaapi.dtos.PromedioTutorDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.CalificacionesPorPuntuacionDTO;

import tutorconecta.com.example.tutorconectaapi.components.EntityMapper;
import tutorconecta.com.example.tutorconectaapi.dtos.CalificacionRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.CalificacionResponseDTO;
import tutorconecta.com.example.tutorconectaapi.entities.Calificacion;
import tutorconecta.com.example.tutorconectaapi.repositories.CalificacionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalificacionServiceImpl implements CalificacionService {
    private final CalificacionRepository repository;
    private final EntityMapper mapper;

    public CalificacionServiceImpl(CalificacionRepository repository, EntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CalificacionResponseDTO crear(CalificacionRequestDTO request) {
        return mapper.toCalificacionResponseDTO(repository.save(mapper.toCalificacionEntity(request)));
    }

    @Override
    public List<CalificacionResponseDTO> listarTodos() {
        return repository.findAll().stream().map(mapper::toCalificacionResponseDTO).toList();
    }

    @Override
    public CalificacionResponseDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toCalificacionResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("No existe una calificacion con ID: " + id));
    }

    @Override
    public CalificacionResponseDTO actualizar(Long id, CalificacionRequestDTO request) {
        buscarPorId(id);
        Calificacion calificacion = mapper.toCalificacionEntity(request);
        calificacion.setIdCalificacion(id);
        return mapper.toCalificacionResponseDTO(repository.save(calificacion));
    }

    @Override
    public void eliminar(Long id) {
        Calificacion calificacion = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe una calificacion con ID: " + id));
        repository.delete(calificacion);
    }

@Override
    public List<PromedioTutorDTO> reportePromedioPorTutor() {
        return repository.promedioPorTutor().stream()
                .map(f -> new PromedioTutorDTO(((Number) f[0]).longValue(), (String) f[1], ((Number) f[2]).longValue(), new BigDecimal(f[3].toString())))
                .toList();
    }

    @Override
    public List<CalificacionesPorPuntuacionDTO> reportePorPuntuacion() {
        return repository.contarPorPuntuacion().stream()
                .map(f -> new CalificacionesPorPuntuacionDTO(((Number) f[0]).intValue(), ((Number) f[1]).longValue()))
                .toList();
    }
}
