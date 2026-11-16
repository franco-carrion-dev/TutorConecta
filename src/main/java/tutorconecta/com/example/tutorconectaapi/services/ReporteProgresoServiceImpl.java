package tutorconecta.com.example.tutorconectaapi.services;

import tutorconecta.com.example.tutorconectaapi.components.EntityMapper;
import tutorconecta.com.example.tutorconectaapi.dtos.ReporteProgresoRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.ReporteProgresoResponseDTO;
import tutorconecta.com.example.tutorconectaapi.entities.ReporteProgreso;
import tutorconecta.com.example.tutorconectaapi.repositories.ReporteProgresoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReporteProgresoServiceImpl implements ReporteProgresoService {
    private final ReporteProgresoRepository repository;
    private final EntityMapper mapper;

    public ReporteProgresoServiceImpl(ReporteProgresoRepository repository, EntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ReporteProgresoResponseDTO crear(ReporteProgresoRequestDTO request) {
        return mapper.toReporteResponseDTO(repository.save(mapper.toReporteEntity(request)));
    }

    @Override
    public List<ReporteProgresoResponseDTO> listarTodos() {
        return repository.findAll().stream().map(mapper::toReporteResponseDTO).toList();
    }

    @Override
    public ReporteProgresoResponseDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toReporteResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("No existe un reporte con ID: " + id));
    }

    @Override
    public ReporteProgresoResponseDTO actualizar(Long id, ReporteProgresoRequestDTO request) {
        buscarPorId(id);
        ReporteProgreso reporte = mapper.toReporteEntity(request);
        reporte.setIdReporte(id);
        return mapper.toReporteResponseDTO(repository.save(reporte));
    }

    @Override
    public void eliminar(Long id) {
        ReporteProgreso reporte = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe un reporte con ID: " + id));
        repository.delete(reporte);
    }

    @Override
    public List<ReporteProgreso> findReportesByEstudianteId(Long idEstudiante) {
        return repository.findReportesByEstudianteId(idEstudiante);
    }

    @Override
    public List<ReporteProgreso> findReportesByTutorId(Long idTutor) {
        return repository.findReportesByTutorId(idTutor);
    }


}
