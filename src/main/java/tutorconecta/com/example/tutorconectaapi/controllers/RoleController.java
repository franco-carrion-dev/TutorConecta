package tutorconecta.com.example.tutorconectaapi.controllers;

import java.math.BigDecimal;
import org.springframework.web.bind.annotation.GetMapping;
import tutorconecta.com.example.tutorconectaapi.dtos.UsuariosPorRolDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.PorcentajeRolDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tutorconecta.com.example.tutorconectaapi.entities.Role;
import tutorconecta.com.example.tutorconectaapi.repositories.RoleRepository;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Roles")
public class RoleController {
    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    @Operation(summary = "Listar roles")
    public ResponseEntity<List<Role>> listar() {
        return ResponseEntity.ok(roleRepository.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar rol por ID")
    public ResponseEntity<Role> buscarPorId(@PathVariable Long id) {
        return roleRepository.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Registrar rol")
    public ResponseEntity<Role> crear(@RequestBody Role role) {
        role.setIdRol(null);
        Role creado = roleRepository.save(role);
        return ResponseEntity.created(URI.create("/api/roles/" + creado.getIdRol())).body(creado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar rol")
    public ResponseEntity<Role> actualizar(@PathVariable Long id, @RequestBody Role role) {
        if (!roleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        role.setIdRol(id);
        return ResponseEntity.ok(roleRepository.save(role));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar rol")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!roleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        roleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

@GetMapping("/reporte/usuarios-por-rol")
    @Operation(summary = "Reporte: total de usuarios por rol (activos e inactivos)")
    public ResponseEntity<List<UsuariosPorRolDTO>> reporteUsuariosPorRol() {
        List<UsuariosPorRolDTO> lista = roleRepository.contarUsuariosPorRol().stream()
                .map(f -> new UsuariosPorRolDTO(((Number) f[0]).longValue(), (String) f[1], ((Number) f[2]).longValue(), ((Number) f[3]).longValue(), ((Number) f[4]).longValue()))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/reporte/porcentaje-por-rol")
    @Operation(summary = "Reporte: porcentaje de usuarios por rol")
    public ResponseEntity<List<PorcentajeRolDTO>> reportePorcentajePorRol() {
        List<PorcentajeRolDTO> lista = roleRepository.porcentajeUsuariosPorRol().stream()
                .map(f -> new PorcentajeRolDTO((String) f[0], ((Number) f[1]).longValue(), new BigDecimal(f[2].toString())))
                .toList();
        return ResponseEntity.ok(lista);
    }
}
