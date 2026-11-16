package tutorconecta.com.example.tutorconectaapi.controllers;

import tutorconecta.com.example.tutorconectaapi.dtos.CalificacionRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.CalificacionResponseDTO;
import tutorconecta.com.example.tutorconectaapi.services.CalificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/calificaciones")
@Tag(name = "Calificaciones")
public class CalificacionController {
    private final CalificacionService calificacionService;

    public CalificacionController(CalificacionService calificacionService) {
        this.calificacionService = calificacionService;
    }

    @PostMapping
    @Operation(summary = "Crear una calificacion")
    public ResponseEntity<CalificacionResponseDTO> crear(
            @Valid @RequestBody CalificacionRequestDTO calificacion) {
        CalificacionResponseDTO creada = calificacionService.crear(calificacion);
        return ResponseEntity.created(URI.create("/api/calificaciones/" + creada.getIdCalificacion())).body(creada);
    }

    @GetMapping
    @Operation(summary = "Listar todas las calificaciones")
    public ResponseEntity<List<CalificacionResponseDTO>> listarTodos() {
        return ResponseEntity.ok(calificacionService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar una calificacion por ID")
    public ResponseEntity<CalificacionResponseDTO> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(calificacionService.buscarPorId(id));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una calificacion")
    public ResponseEntity<CalificacionResponseDTO> actualizar(
            @PathVariable Long id, @Valid @RequestBody CalificacionRequestDTO calificacion) {
        try {
            return ResponseEntity.ok(calificacionService.actualizar(id, calificacion));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una calificacion")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            calificacionService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }
}
