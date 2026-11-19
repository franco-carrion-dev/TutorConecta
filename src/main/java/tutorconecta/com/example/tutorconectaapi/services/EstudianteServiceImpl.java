package tutorconecta.com.example.tutorconectaapi.services;

import tutorconecta.com.example.tutorconectaapi.components.EntityMapper;
import tutorconecta.com.example.tutorconectaapi.dtos.EstudianteRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.EstudianteResponseDTO;
import tutorconecta.com.example.tutorconectaapi.entities.Estudiante;
import tutorconecta.com.example.tutorconectaapi.repositories.EstudianteRepository;
import tutorconecta.com.example.tutorconectaapi.repositories.UsuarioRepository;
import tutorconecta.com.example.tutorconectaapi.entities.Usuario;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstudianteServiceImpl implements EstudianteService {
    private final EstudianteRepository repository;
    private final EntityMapper mapper;
    private final UsuarioRepository usuarioRepository;

    public EstudianteServiceImpl(EstudianteRepository repository, EntityMapper mapper, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public EstudianteResponseDTO crear(EstudianteRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new EntityNotFoundException("No existe un usuario con ID: " + request.getIdUsuario()));
        if (repository.existsById(usuario.getIdUsuario())) {
            throw new IllegalArgumentException("El usuario ya tiene un perfil registrado");
        }
        Estudiante estudiante = mapper.toEstudianteEntity(request);
        // Con @MapsId el ID se toma del usuario: se deja null para que JPA haga persist (no merge)
        estudiante.setIdEstudiante(null);
        estudiante.setUsuario(usuario);
        return mapper.toEstudianteResponseDTO(repository.save(estudiante));
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
