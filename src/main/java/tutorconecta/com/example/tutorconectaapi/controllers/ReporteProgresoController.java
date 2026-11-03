package tutorconecta.com.example.tutorconectaapi.controllers;

import tutorconecta.com.example.tutorconectaapi.dtos.ReporteProgresoRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.ReporteProgresoResponseDTO;
import tutorconecta.com.example.tutorconectaapi.servicesinterfaces.ReporteProgresoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reportes-progreso")
@Tag(name = "Reportes de progreso")
public class ReporteProgresoController {
    private final ReporteProgresoService reporteProgresoService;

    public ReporteProgresoController(ReporteProgresoService reporteProgresoService) {
        this.reporteProgresoService = reporteProgresoService;
    }

    @PostMapping
    @Operation(summary = "Crear un reporte de progreso")
    public ResponseEntity<ReporteProgresoResponseDTO> crear(
            @Valid @RequestBody ReporteProgresoRequestDTO reporte) {
        ReporteProgresoResponseDTO creado = reporteProgresoService.crear(reporte);
        return ResponseEntity.created(
                URI.create("/api/reportes-progreso/" + creado.getIdReporte())).body(creado);
    }

    @GetMapping
    @Operation(summary = "Listar todos los reportes de progreso")
    public ResponseEntity<List<ReporteProgresoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(reporteProgresoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar un reporte de progreso por ID")
    public ResponseEntity<ReporteProgresoResponseDTO> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(reporteProgresoService.buscarPorId(id));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un reporte de progreso")
    public ResponseEntity<ReporteProgresoResponseDTO> actualizar(
            @PathVariable Long id, @Valid @RequestBody ReporteProgresoRequestDTO reporte) {
        try {
            return ResponseEntity.ok(reporteProgresoService.actualizar(id, reporte));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un reporte de progreso")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            reporteProgresoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }
}
