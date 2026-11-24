package tutorconecta.com.example.tutorconectaapi.services;

import tutorconecta.com.example.tutorconectaapi.components.EntityMapper;
import tutorconecta.com.example.tutorconectaapi.dtos.UsuarioRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.UsuarioResponseDTO;
import tutorconecta.com.example.tutorconectaapi.entities.Usuario;
import tutorconecta.com.example.tutorconectaapi.repositories.UsuarioRepository;
import tutorconecta.com.example.tutorconectaapi.repositories.TutorRepository;
import tutorconecta.com.example.tutorconectaapi.repositories.EstudianteRepository;
import tutorconecta.com.example.tutorconectaapi.repositories.SesionRepository;
import tutorconecta.com.example.tutorconectaapi.repositories.DisponibilidadHorariaRepository;
import tutorconecta.com.example.tutorconectaapi.entities.Tutor;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final EntityMapper entityMapper;
    private final PasswordEncoder passwordEncoder;
    private final TutorRepository tutorRepository;
    private final EstudianteRepository estudianteRepository;
    private final SesionRepository sesionRepository;
    private final DisponibilidadHorariaRepository disponibilidadRepository;

    public UsuarioServiceImpl(
            UsuarioRepository usuarioRepository,
            EntityMapper entityMapper,
            PasswordEncoder passwordEncoder,
            TutorRepository tutorRepository,
            EstudianteRepository estudianteRepository,
            SesionRepository sesionRepository,
            DisponibilidadHorariaRepository disponibilidadRepository) {
        this.usuarioRepository = usuarioRepository;
        this.entityMapper = entityMapper;
        this.passwordEncoder = passwordEncoder;
        this.tutorRepository = tutorRepository;
        this.estudianteRepository = estudianteRepository;
        this.sesionRepository = sesionRepository;
        this.disponibilidadRepository = disponibilidadRepository;
    }

    @Override
    public UsuarioResponseDTO crear(UsuarioRequestDTO request) {
        if (request.getPasswordHash() == null || request.getPasswordHash().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        Usuario usuario = entityMapper.toUsuarioEntity(request);
        usuario.setPasswordHash(passwordEncoder.encode(request.getPasswordHash()));
        if (usuario.getActivo() == null) usuario.setActivo(true);
        if (usuario.getFechaRegistro() == null) usuario.setFechaRegistro(java.time.LocalDateTime.now());
        return entityMapper.toUsuarioResponseDTO(usuarioRepository.save(usuario));
    }

    @Override
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(entityMapper::toUsuarioResponseDTO)
                .toList();
    }

    @Override
    public UsuarioResponseDTO buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(entityMapper::toUsuarioResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("No existe un usuario con ID: " + id));
    }

    @Override
    public UsuarioResponseDTO actualizar(Long id, UsuarioRequestDTO request) {
        Usuario actual = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe un usuario con ID: " + id));
        Usuario usuario = entityMapper.toUsuarioEntity(request);
        usuario.setIdUsuario(id);
        if (request.getPasswordHash() == null || request.getPasswordHash().isBlank()) {
            usuario.setPasswordHash(actual.getPasswordHash());
        } else {
            usuario.setPasswordHash(passwordEncoder.encode(request.getPasswordHash()));
        }
        if (usuario.getFechaRegistro() == null) usuario.setFechaRegistro(actual.getFechaRegistro());
        if (usuario.getActivo() == null) usuario.setActivo(actual.getActivo());
        return entityMapper.toUsuarioResponseDTO(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("No existe un usuario con ID: " + id);
        }
        // Si participa en sesiones no se borra: se perdería el historial de pagos y calificaciones
        if (sesionRepository.existsByEstudianteIdEstudiante(id) || sesionRepository.existsByTutorIdTutor(id)) {
            throw new IllegalArgumentException(
                    "No se puede eliminar: el usuario tiene sesiones registradas. Desactívalo en su lugar.");
        }
        // Primero se borran los datos que dependen del usuario (perfil y disponibilidad)
        Tutor tutor = tutorRepository.findById(id).orElse(null);
        if (tutor != null) {
            disponibilidadRepository.deleteAll(disponibilidadRepository.findDisponibilidadByTutorId(id));
            if (tutor.getMaterias() != null) tutor.getMaterias().clear();
            tutorRepository.delete(tutor);
        }
        estudianteRepository.findById(id).ifPresent(estudianteRepository::delete);
        usuarioRepository.flush();
        usuarioRepository.deleteById(id);
    }

    @Override
    public List<UsuarioResponseDTO> buscarTutorPorNombre(String nombre) {
        return usuarioRepository.findTutorByNombre(nombre).stream()
                .map(entityMapper::toUsuarioResponseDTO)
                .toList();
    }

}
