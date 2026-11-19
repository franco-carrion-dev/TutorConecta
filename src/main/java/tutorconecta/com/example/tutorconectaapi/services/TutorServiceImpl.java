package tutorconecta.com.example.tutorconectaapi.services;

import tutorconecta.com.example.tutorconectaapi.components.EntityMapper;
import tutorconecta.com.example.tutorconectaapi.dtos.TutorRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.TutorResponseDTO;
import tutorconecta.com.example.tutorconectaapi.entities.Tutor;
import tutorconecta.com.example.tutorconectaapi.repositories.TutorRepository;
import tutorconecta.com.example.tutorconectaapi.repositories.UsuarioRepository;
import tutorconecta.com.example.tutorconectaapi.entities.Usuario;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TutorServiceImpl implements TutorService {
    private final TutorRepository repository;
    private final EntityMapper mapper;
    private final UsuarioRepository usuarioRepository;

    public TutorServiceImpl(TutorRepository repository, EntityMapper mapper, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public TutorResponseDTO crear(TutorRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new EntityNotFoundException("No existe un usuario con ID: " + request.getIdUsuario()));
        if (repository.existsById(usuario.getIdUsuario())) {
            throw new IllegalArgumentException("El usuario ya tiene un perfil registrado");
        }
        Tutor tutor = mapper.toTutorEntity(request);
        // Con @MapsId el ID se toma del usuario: se deja null para que JPA haga persist (no merge)
        tutor.setIdTutor(null);
        tutor.setUsuario(usuario);
        return mapper.toTutorResponseDTO(repository.save(tutor));
    }

    @Override
    public List<TutorResponseDTO> listarTodos() {
        return repository.findAll().stream().map(mapper::toTutorResponseDTO).toList();
    }

    @Override
    public TutorResponseDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toTutorResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("No existe un tutor con ID: " + id));
    }

    @Override
    public TutorResponseDTO actualizar(Long id, TutorRequestDTO request) {
        buscarPorId(id);
        Tutor tutor = mapper.toTutorEntity(request);
        tutor.setIdTutor(id);
        return mapper.toTutorResponseDTO(repository.save(tutor));
    }

    @Override
    public void eliminar(Long id) {
        Tutor tutor = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe un tutor con ID: " + id));
        repository.delete(tutor);
    }
}
