package tutorconecta.com.example.tutorconectaapi.controllers;

import java.math.BigDecimal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tutorconecta.com.example.tutorconectaapi.dtos.GastoEstudianteDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tutorconecta.com.example.tutorconectaapi.dtos.EstudianteRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.EstudianteResponseDTO;
import tutorconecta.com.example.tutorconectaapi.services.EstudianteService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
@Tag(name = "Estudiantes")
public class EstudianteController {
    private final EstudianteService service;

    public EstudianteController(EstudianteService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Registrar estudiante")
    public ResponseEntity<EstudianteResponseDTO> crear(@Valid @RequestBody EstudianteRequestDTO request) {
        EstudianteResponseDTO creado = service.crear(request);
        return ResponseEntity.created(URI.create("/api/estudiantes/" + creado.getIdEstudiante())).body(creado);
    }

    @GetMapping
    @Operation(summary = "Listar estudiantes")
    public ResponseEntity<List<EstudianteResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar estudiante por ID")
    public ResponseEntity<EstudianteResponseDTO> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.buscarPorId(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar estudiante")
    public ResponseEntity<EstudianteResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody EstudianteRequestDTO request) {
        try {
            return ResponseEntity.ok(service.actualizar(id, request));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar estudiante")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

@GetMapping("/reporte/gastos")
    @Operation(summary = "Reporte: gasto total por estudiante (pagos completados)")
    public ResponseEntity<List<GastoEstudianteDTO>> reporteGastos() {
        return ResponseEntity.ok(service.reporteGastos());
    }

    @GetMapping("/presupuesto")
    @Operation(summary = "Buscar estudiantes con presupuesto máximo mayor o igual a un monto")
    public ResponseEntity<List<EstudianteResponseDTO>> buscarPorPresupuestoMinimo(@RequestParam BigDecimal monto) {
        return ResponseEntity.ok(service.buscarPorPresupuestoMinimo(monto));
    }
}
