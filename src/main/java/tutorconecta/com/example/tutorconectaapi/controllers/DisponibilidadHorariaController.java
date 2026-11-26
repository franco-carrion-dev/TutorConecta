package tutorconecta.com.example.tutorconectaapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import tutorconecta.com.example.tutorconectaapi.dtos.HorasPorDiaDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.HorasPorTutorDTO;

import tutorconecta.com.example.tutorconectaapi.dtos.DisponibilidadHorariaRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.DisponibilidadHorariaResponseDTO;
import tutorconecta.com.example.tutorconectaapi.components.EntityMapper;
import tutorconecta.com.example.tutorconectaapi.services.DisponibilidadHorariaService;
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
    private final EntityMapper mapper;

    public DisponibilidadHorariaController(DisponibilidadHorariaService disponibilidadHorariaService,
                                           EntityMapper mapper) {
        this.disponibilidadHorariaService = disponibilidadHorariaService;
        this.mapper = mapper;
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
    public ResponseEntity<List<DisponibilidadHorariaResponseDTO>> buscarPorTutorId(@PathVariable Long idTutor) {
        List<DisponibilidadHorariaResponseDTO> disponibilidades = disponibilidadHorariaService.findDisponibilidadByTutorId(idTutor)
                .stream().map(mapper::toDisponibilidadResponseDTO).toList();
        if (disponibilidades.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(disponibilidades);
    }

    @GetMapping("/dia/{diaSemana}")
    @Operation(summary = "Listar disponibilidades horarias por día de la semana")
    public ResponseEntity<List<DisponibilidadHorariaResponseDTO>> buscarPorDiaSemana(@PathVariable String diaSemana) {
        List<DisponibilidadHorariaResponseDTO> disponibilidades = disponibilidadHorariaService.findByDiaSemana(diaSemana)
                .stream().map(mapper::toDisponibilidadResponseDTO).toList();
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

@GetMapping("/reporte/horas-por-dia")
    @Operation(summary = "Reporte: horas disponibles por día de la semana")
    public ResponseEntity<List<HorasPorDiaDTO>> reporteHorasPorDia() {
        return ResponseEntity.ok(disponibilidadHorariaService.reporteHorasPorDia());
    }

    @GetMapping("/reporte/horas-por-tutor")
    @Operation(summary = "Reporte: horas disponibles a la semana por tutor")
    public ResponseEntity<List<HorasPorTutorDTO>> reporteHorasPorTutor() {
        return ResponseEntity.ok(disponibilidadHorariaService.reporteHorasPorTutor());
    }
}
