package tutorconecta.com.example.tutorconectaapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import tutorconecta.com.example.tutorconectaapi.dtos.UsuarioRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.UsuarioResponseDTO;
import tutorconecta.com.example.tutorconectaapi.servicesinterfaces.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
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
}
