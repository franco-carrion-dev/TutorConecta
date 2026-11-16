package tutorconecta.com.example.tutorconectaapi.services;

import tutorconecta.com.example.tutorconectaapi.components.EntityMapper;
import tutorconecta.com.example.tutorconectaapi.dtos.EstudianteRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.EstudianteResponseDTO;
import tutorconecta.com.example.tutorconectaapi.entities.Estudiante;
import tutorconecta.com.example.tutorconectaapi.repositories.EstudianteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstudianteServiceImpl implements EstudianteService {
    private final EstudianteRepository repository;
    private final EntityMapper mapper;

    public EstudianteServiceImpl(EstudianteRepository repository, EntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public EstudianteResponseDTO crear(EstudianteRequestDTO request) {
        return mapper.toEstudianteResponseDTO(repository.save(mapper.toEstudianteEntity(request)));
    }

    @Override
    public List<EstudianteResponseDTO> listarTodos() {
        return repository.findAll().stream().map(mapper::toEstudianteResponseDTO).toList();
    }

    @Override
    public EstudianteResponseDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toEstudianteResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("No existe un estudiante con ID: " + id));
    }

    @Override
    public EstudianteResponseDTO actualizar(Long id, EstudianteRequestDTO request) {
        buscarPorId(id);
        Estudiante estudiante = mapper.toEstudianteEntity(request);
        estudiante.setIdEstudiante(id);
        return mapper.toEstudianteResponseDTO(repository.save(estudiante));
    }

    @Override
    public void eliminar(Long id) {
        Estudiante estudiante = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe un estudiante con ID: " + id));
        repository.delete(estudiante);
    }
}
