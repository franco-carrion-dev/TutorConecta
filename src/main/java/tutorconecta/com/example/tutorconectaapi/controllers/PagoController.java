package tutorconecta.com.example.tutorconectaapi.controllers;

import tutorconecta.com.example.tutorconectaapi.dtos.PagoRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.PagoResponseDTO;
import tutorconecta.com.example.tutorconectaapi.services.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@Tag(name = "Pagos")
public class PagoController {
    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    @Operation(summary = "Crear un pago")
    public ResponseEntity<PagoResponseDTO> crear(@Valid @RequestBody PagoRequestDTO pago) {
        PagoResponseDTO creado = pagoService.crear(pago);
        return ResponseEntity.created(URI.create("/api/pagos/" + creado.getIdPago())).body(creado);
    }

    @GetMapping
    @Operation(summary = "Listar todos los pagos")
    public ResponseEntity<List<PagoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(pagoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar un pago por ID")
    public ResponseEntity<PagoResponseDTO> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pagoService.buscarPorId(id));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un pago")
    public ResponseEntity<PagoResponseDTO> actualizar(
            @PathVariable Long id, @Valid @RequestBody PagoRequestDTO pago) {
        try {
            return ResponseEntity.ok(pagoService.actualizar(id, pago));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un pago")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            pagoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Buscar pagos por su estado")
    public ResponseEntity<List<PagoResponseDTO>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(pagoService.buscarPorEstado(estado));
    }

    @GetMapping("/tutor/{idTutor}/estado/{estado}")
    @Operation(summary = "Buscar pagos de un tutor filtrados por su estado")
    public ResponseEntity<List<PagoResponseDTO>> buscarPorTutorYEstado(
            @PathVariable Long idTutor,
            @PathVariable String estado) {
        return ResponseEntity.ok(pagoService.buscarPorTutorYEstado(idTutor, estado));
    }
}
