package tutorconecta.com.example.tutorconectaapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tutorconecta.com.example.tutorconectaapi.dtos.MateriaRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.MateriaResponseDTO;
import tutorconecta.com.example.tutorconectaapi.services.MateriaService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/materias")
@Tag(name = "Materias")
@PreAuthorize("hasRole('ADMIN')")
public class MateriaController {
    private final MateriaService materiaService;

    public MateriaController(MateriaService materiaService) {
        this.materiaService = materiaService;
    }

    @PostMapping
    @Operation(summary = "Crear una materia")
    public ResponseEntity<MateriaResponseDTO> crear(
            @Valid @RequestBody MateriaRequestDTO request) {
        MateriaResponseDTO creada = materiaService.crear(request);
        return ResponseEntity.created(URI.create("/api/materias/" + creada.getIdMateria())).body(creada);
    }

    @GetMapping
    @Operation(summary = "Listar todas las materias")
    public ResponseEntity<List<MateriaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(materiaService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar una materia por ID")
    public ResponseEntity<MateriaResponseDTO> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(materiaService.buscarPorId(id));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una materia")
    public ResponseEntity<MateriaResponseDTO> actualizar(
            @PathVariable Long id, @Valid @RequestBody MateriaRequestDTO request) {
        try {
            return ResponseEntity.ok(materiaService.actualizar(id, request));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una materia")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            materiaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }
}
