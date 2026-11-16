package tutorconecta.com.example.tutorconectaapi.services;

import tutorconecta.com.example.tutorconectaapi.components.EntityMapper;
import tutorconecta.com.example.tutorconectaapi.dtos.UsuarioRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.UsuarioResponseDTO;
import tutorconecta.com.example.tutorconectaapi.entities.Usuario;
import tutorconecta.com.example.tutorconectaapi.repositories.UsuarioRepository;
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

    public UsuarioServiceImpl(
            UsuarioRepository usuarioRepository,
            EntityMapper entityMapper,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.entityMapper = entityMapper;
        this.passwordEncoder = passwordEncoder;
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
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("No existe un usuario con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public List<UsuarioResponseDTO> buscarTutorPorNombre(String nombre) {
        return usuarioRepository.findTutorByNombre(nombre).stream()
                .map(entityMapper::toUsuarioResponseDTO)
                .toList();
    }

}
