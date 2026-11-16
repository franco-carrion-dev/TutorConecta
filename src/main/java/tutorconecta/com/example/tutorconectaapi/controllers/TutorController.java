package tutorconecta.com.example.tutorconectaapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tutorconecta.com.example.tutorconectaapi.dtos.TutorRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.TutorResponseDTO;
import tutorconecta.com.example.tutorconectaapi.services.TutorService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tutores")
@Tag(name = "Tutors")
public class TutorController {
    private final TutorService service;

    public TutorController(TutorService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Registrar tutor")
    public ResponseEntity<TutorResponseDTO> crear(@Valid @RequestBody TutorRequestDTO request) {
        TutorResponseDTO creado = service.crear(request);
        return ResponseEntity.created(URI.create("/api/tutores/" + creado.getIdTutor())).body(creado);
    }

    @GetMapping
    @Operation(summary = "Listar tutores")
    public ResponseEntity<List<TutorResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tutor por ID")
    public ResponseEntity<TutorResponseDTO> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.buscarPorId(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tutor")
    public ResponseEntity<TutorResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody TutorRequestDTO request) {
        try {
            return ResponseEntity.ok(service.actualizar(id, request));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tutor")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
