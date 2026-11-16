package tutorconecta.com.example.tutorconectaapi.controllers;

import tutorconecta.com.example.tutorconectaapi.dtos.LoginRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.RegistroRequestDTO;
import tutorconecta.com.example.tutorconectaapi.entities.Estudiante;
import tutorconecta.com.example.tutorconectaapi.entities.Role;
import tutorconecta.com.example.tutorconectaapi.entities.Tutor;
import tutorconecta.com.example.tutorconectaapi.entities.Usuario;
import tutorconecta.com.example.tutorconectaapi.repositories.EstudianteRepository;
import tutorconecta.com.example.tutorconectaapi.repositories.RoleRepository;
import tutorconecta.com.example.tutorconectaapi.repositories.TutorRepository;
import tutorconecta.com.example.tutorconectaapi.repositories.UsuarioRepository;
import tutorconecta.com.example.tutorconectaapi.security.JwtUtil;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final TutorRepository tutorRepository;
    private final EstudianteRepository estudianteRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                          RoleRepository roleRepository, TutorRepository tutorRepository,
                          EstudianteRepository estudianteRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.tutorRepository = tutorRepository;
        this.estudianteRepository = estudianteRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String username = authentication.getName();
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String token = jwtUtil.generateToken(username, roles);
        Usuario usuario = usuarioRepository.findByEmail(username);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("roles", roles);
        if (usuario != null) {
            response.put("idUsuario", usuario.getIdUsuario());
            response.put("nombre", usuario.getNombre() + " " + usuario.getApellido());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registro")
    @Transactional
    public ResponseEntity<Map<String, Object>> registro(@Valid @RequestBody RegistroRequestDTO request) {
        String rolPedido = request.getRol().trim().toUpperCase();
        if (!rolPedido.equals("TUTOR") && !rolPedido.equals("ESTUDIANTE")) {
            return ResponseEntity.badRequest().body(Map.of("error", "El rol debe ser TUTOR o ESTUDIANTE"));
        }
        if (usuarioRepository.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "El email ya está registrado"));
        }
        Role rol = roleRepository.findByDenominacionIgnoreCase(rolPedido)
                .orElseGet(() -> {
                    Role nuevo = new Role();
                    nuevo.setDenominacion(rolPedido);
                    return roleRepository.save(nuevo);
                });

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        usuario.setActivo(true);
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setRol(rol);
        usuario = usuarioRepository.save(usuario);

        if (rolPedido.equals("TUTOR")) {
            Tutor tutor = new Tutor();
            tutor.setUsuario(usuario);
            tutor.setBiografia(request.getBiografia());
            tutor.setTarifaPorHora(request.getTarifaPorHora() != null ? request.getTarifaPorHora() : BigDecimal.ZERO);
            tutor.setVerificado(false); // el admin valida la identidad antes de publicar el perfil
            tutor.setCalificacionPromedio(BigDecimal.ZERO);
            tutorRepository.save(tutor);
        } else {
            Estudiante estudiante = new Estudiante();
            estudiante.setUsuario(usuario);
            estudiante.setPresupuestoMaximo(request.getPresupuestoMaximo());
            estudianteRepository.save(estudiante);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("idUsuario", usuario.getIdUsuario());
        response.put("mensaje", "Usuario registrado correctamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
