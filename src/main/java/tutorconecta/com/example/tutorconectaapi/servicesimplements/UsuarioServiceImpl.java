package tutorconecta.com.example.tutorconectaapi.servicesimplements;

import tutorconecta.com.example.tutorconectaapi.components.EntityMapper;
import tutorconecta.com.example.tutorconectaapi.dtos.UsuarioRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.UsuarioResponseDTO;
import tutorconecta.com.example.tutorconectaapi.entities.Usuario;
import tutorconecta.com.example.tutorconectaapi.repositories.UsuarioRepository;
import tutorconecta.com.example.tutorconectaapi.servicesinterfaces.UsuarioService;
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
        Usuario usuario = entityMapper.toUsuarioEntity(request);
        usuario.setPasswordHash(passwordEncoder.encode(request.getPasswordHash()));
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
        buscarPorId(id);
        Usuario usuario = entityMapper.toUsuarioEntity(request);
        usuario.setIdUsuario(id);
        usuario.setPasswordHash(passwordEncoder.encode(request.getPasswordHash()));
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
