package tutorconecta.com.example.tutorconectaapi.controllers; // Paquete corregido

import tutorconecta.com.example.tutorconectaapi.dtos.SesionRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.SesionResponseDTO;
import tutorconecta.com.example.tutorconectaapi.services.SesionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/sesiones")
public class SesionController {

    private final SesionService sesionService;

    public SesionController(SesionService sesionService) {
        this.sesionService = sesionService;
    }

    @PostMapping
    public ResponseEntity<SesionResponseDTO> crearSesion(@Valid @RequestBody SesionRequestDTO request) {
        SesionResponseDTO creada = sesionService.crear(request);
        return ResponseEntity.created(URI.create("/api/sesiones/" + creada.getIdSesion())).body(creada);
    }

    @GetMapping
    public ResponseEntity<List<SesionResponseDTO>> listarSesiones() {
        return ResponseEntity.ok(sesionService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SesionResponseDTO> obtenerSesion(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(sesionService.buscarPorId(id));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SesionResponseDTO> actualizarSesion(
            @PathVariable Long id, @Valid @RequestBody SesionRequestDTO request) {
        try {
            return ResponseEntity.ok(sesionService.actualizar(id, request));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> actualizarEstado(
            @PathVariable Long id, @RequestParam String estado) {
        try {
            sesionService.updateEstadoSesion(id, estado);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSesion(@PathVariable Long id) {
        try {
            sesionService.eliminar(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }
}