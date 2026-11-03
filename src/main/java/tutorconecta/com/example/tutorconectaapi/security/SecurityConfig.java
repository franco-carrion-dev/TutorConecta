package tutorconecta.com.example.tutorconectaapi.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import tutorconecta.com.example.tutorconectaapi.entities.Usuario;
import tutorconecta.com.example.tutorconectaapi.repositories.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CustomJwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
        http
                .cors(Customizer.withDefaults()) // Habilita el manejo básico de CORS
                .csrf(csrf -> csrf.disable()) // Deshabilita la protección de falsificación para APIs
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. EL SALVAVIDAS: Permitir peticiones fantasma (Preflight OPTIONS) de los navegadores/Swagger
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        
                        // 2. Rutas públicas
                        .requestMatchers(
                                "/api/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html")
                        .permitAll()
                        
                        // 3. Rutas protegidas globales (Asegura usar Role, Spring busca el prefijo ROLE_ internamente)
                        .requestMatchers("/api/reportes/**", "/api/reportes-progreso/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**")
                        .hasRole("ADMIN")
                        
                        // 4. Cualquier otra requiere estar logueado
                        .anyRequest().authenticated())
                .oauth2ResourceServer(resourceServer -> resourceServer
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));

        return http.build();
    }

    @Bean
    public CustomJwtAuthenticationConverter jwtAuthenticationConverter() {
        return new CustomJwtAuthenticationConverter();
    }

    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return username -> {
            Usuario usuario = usuarioRepository.findByEmail(username);
            if (usuario == null) {
                throw new org.springframework.security.core.userdetails.UsernameNotFoundException(
                        "Usuario no encontrado: " + username);
            }

            // Aquí es donde nace el ROLE_ADMIN. Funciona perfecto, pero exige usar hasRole()
            String authority = usuario.getRol() == null
                    ? "ROLE_ESTUDIANTE"
                    : "ROLE_" + usuario.getRol().getDenominacion().trim().toUpperCase();
            return User.withUsername(usuario.getEmail())
                    .password(usuario.getPasswordHash())
                    .authorities(new SimpleGrantedAuthority(authority))
                    .disabled(Boolean.FALSE.equals(usuario.getActivo()))
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
