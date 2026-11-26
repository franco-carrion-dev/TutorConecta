package tutorconecta.com.example.tutorconectaapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import tutorconecta.com.example.tutorconectaapi.dtos.UsuariosPorMesDTO;

import io.swagger.v3.oas.annotations.Operation;
import tutorconecta.com.example.tutorconectaapi.dtos.UsuarioRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.UsuarioResponseDTO;
import tutorconecta.com.example.tutorconectaapi.services.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import tutorconecta.com.example.tutorconectaapi.repositories.UsuarioRepository;
import tutorconecta.com.example.tutorconectaapi.components.EntityMapper;
import tutorconecta.com.example.tutorconectaapi.entities.Usuario;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final EntityMapper mapper;

    public UsuarioController(UsuarioService usuarioService, UsuarioRepository usuarioRepository,
                             EntityMapper mapper) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "Listar todos los usuarios")
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/me")
    @Operation(summary = "Datos del usuario autenticado (según el JWT)")
    public ResponseEntity<UsuarioResponseDTO> yo(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        Usuario usuario = usuarioRepository.findByEmail(principal.getName());
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.toUsuarioResponseDTO(usuario));
    }

    @GetMapping("/tutores/buscar")
    @Operation(summary = "Query con Filtro: Buscar tutores por nombre")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarTutoresPorNombre(@RequestParam String nombre) {
        List<UsuarioResponseDTO> tutores = usuarioService.buscarTutorPorNombre(nombre);
        if (tutores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tutores);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO creado = usuarioService.crear(request);
        return ResponseEntity.created(URI.create("/api/usuarios/" + creado.getIdUsuario())).body(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(usuarioService.buscarPorId(id));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
            @PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO request) {
        try {
            return ResponseEntity.ok(usuarioService.actualizar(id, request));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminar(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

@GetMapping("/reporte/registros-por-mes")
    @Operation(summary = "Reporte: cantidad de usuarios registrados por mes en un año")
    public ResponseEntity<List<UsuariosPorMesDTO>> reporteRegistrosPorMes(@RequestParam int anio) {
        return ResponseEntity.ok(usuarioService.reporteRegistrosPorMes(anio));
    }

    @GetMapping("/activo/{activo}")
    @Operation(summary = "Listar usuarios activos (true) o inactivos (false)")
    public ResponseEntity<List<UsuarioResponseDTO>> listarPorActivo(@PathVariable Boolean activo) {
        return ResponseEntity.ok(usuarioService.listarPorActivo(activo));
    }
}
