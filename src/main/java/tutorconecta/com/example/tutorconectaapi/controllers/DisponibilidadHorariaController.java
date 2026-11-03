package tutorconecta.com.example.tutorconectaapi.controllers;

import tutorconecta.com.example.tutorconectaapi.dtos.DisponibilidadHorariaRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.DisponibilidadHorariaResponseDTO;
<<<<<<< HEAD
import tutorconecta.com.example.tutorconectaapi.servicesinterfaces.DisponibilidadHorariaService;
=======
import tutorconecta.com.example.tutorconectaapi.entities.DisponibilidadHoraria;
import tutorconecta.com.example.tutorconectaapi.services.DisponibilidadHorariaService;
>>>>>>> origin/main
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/disponibilidades-horarias")
@Tag(name = "Disponibilidades horarias")
public class DisponibilidadHorariaController {
    private final DisponibilidadHorariaService disponibilidadHorariaService;

    public DisponibilidadHorariaController(DisponibilidadHorariaService disponibilidadHorariaService) {
        this.disponibilidadHorariaService = disponibilidadHorariaService;
    }

    @PostMapping
    @Operation(summary = "Crear una disponibilidad horaria")
    public ResponseEntity<DisponibilidadHorariaResponseDTO> crear(
            @Valid @RequestBody DisponibilidadHorariaRequestDTO disponibilidad) {
        DisponibilidadHorariaResponseDTO creada = disponibilidadHorariaService.crear(disponibilidad);
        return ResponseEntity.created(
                URI.create("/api/disponibilidades-horarias/" + creada.getIdDisponibilidad())).body(creada);
    }

    @GetMapping
    @Operation(summary = "Listar todas las disponibilidades horarias")
    public ResponseEntity<List<DisponibilidadHorariaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(disponibilidadHorariaService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar una disponibilidad horaria por ID")
    public ResponseEntity<DisponibilidadHorariaResponseDTO> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(disponibilidadHorariaService.buscarPorId(id));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/tutor/{idTutor}")
    @Operation(summary = "Listar disponibilidades horarias por ID del tutor")
    public ResponseEntity<List<DisponibilidadHoraria>> buscarPorTutorId(@PathVariable Long idTutor) {
        List<DisponibilidadHoraria> disponibilidades = disponibilidadHorariaService.findDisponibilidadByTutorId(idTutor);
        if (disponibilidades.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(disponibilidades);
    }

    @GetMapping("/dia/{diaSemana}")
    @Operation(summary = "Listar disponibilidades horarias por día de la semana")
    public ResponseEntity<List<DisponibilidadHoraria>> buscarPorDiaSemana(@PathVariable String diaSemana) {
        List<DisponibilidadHoraria> disponibilidades = disponibilidadHorariaService.findByDiaSemana(diaSemana);
        if (disponibilidades.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(disponibilidades);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una disponibilidad horaria")
    public ResponseEntity<DisponibilidadHorariaResponseDTO> actualizar(
            @PathVariable Long id, @Valid @RequestBody DisponibilidadHorariaRequestDTO disponibilidad) {
        try {
            return ResponseEntity.ok(disponibilidadHorariaService.actualizar(id, disponibilidad));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una disponibilidad horaria")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            disponibilidadHorariaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }
}
