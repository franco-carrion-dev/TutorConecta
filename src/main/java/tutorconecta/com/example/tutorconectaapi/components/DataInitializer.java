package tutorconecta.com.example.tutorconectaapi.components;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tutorconecta.com.example.tutorconectaapi.entities.Role;
import tutorconecta.com.example.tutorconectaapi.entities.Usuario;
import tutorconecta.com.example.tutorconectaapi.repositories.RoleRepository;
import tutorconecta.com.example.tutorconectaapi.repositories.UsuarioRepository;

import java.time.LocalDateTime;

/** Crea los roles base y un admin de prueba si la BD está vacía. */
@Component
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Role admin = rol("ADMIN", "Administrador de la plataforma");
        rol("TUTOR", "Tutor que dicta sesiones");
        rol("ESTUDIANTE", "Estudiante que agenda sesiones");

        if (usuarioRepository.findByEmail("admin@tutorconecta.com") == null) {
            Usuario u = new Usuario();
            u.setNombre("Admin");
            u.setApellido("TutorConecta");
            u.setEmail("admin@tutorconecta.com");
            u.setPasswordHash(passwordEncoder.encode("admin123"));
            u.setActivo(true);
            u.setFechaRegistro(LocalDateTime.now());
            u.setRol(admin);
            usuarioRepository.save(u);
        }
    }

    private Role rol(String denominacion, String descripcion) {
        return roleRepository.findAll().stream()
                .filter(r -> r.getDenominacion() != null
                        && r.getDenominacion().trim().toUpperCase().startsWith(denominacion.substring(0, 5)))
                .findFirst()
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setDenominacion(denominacion);
                    r.setDescripcion(descripcion);
                    return roleRepository.save(r);
                });
    }
}
