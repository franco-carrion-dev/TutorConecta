package tutorconecta.com.example.tutorconectaapi.controllers; // Paquete corregido

import java.time.LocalDate;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;
import tutorconecta.com.example.tutorconectaapi.dtos.SesionesPorEstadoDTO;

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

@GetMapping("/reporte/por-estado")
    @Operation(summary = "Reporte: cantidad de sesiones por estado")
    public ResponseEntity<List<SesionesPorEstadoDTO>> reportePorEstado() {
        return ResponseEntity.ok(sesionService.reportePorEstado());
    }

    @GetMapping("/rango")
    @Operation(summary = "Buscar sesiones que inician entre dos fechas (yyyy-MM-dd)")
    public ResponseEntity<List<SesionResponseDTO>> buscarEntreFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(sesionService.buscarEntreFechas(inicio, fin));
    }
}
