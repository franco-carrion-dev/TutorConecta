package tutorconecta.com.example.tutorconectaapi.repositories;

import org.springframework.data.jpa.repository.Query;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import tutorconecta.com.example.tutorconectaapi.entities.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByDenominacionIgnoreCase(String denominacion);

// Reporte: total de usuarios por rol, separando activos e inactivos
    @Query(value = """
            SELECT r.id_rol, r.denominacion,
                   COUNT(u.id_usuario) AS total,
                   COUNT(u.id_usuario) FILTER (WHERE u.activo = true) AS activos,
                   COUNT(u.id_usuario) FILTER (WHERE u.activo IS DISTINCT FROM true) AS inactivos
            FROM roles r
            LEFT JOIN usuarios u ON u.id_rol = r.id_rol
            GROUP BY r.id_rol, r.denominacion
            ORDER BY total DESC
            """, nativeQuery = true)
    List<Object[]> contarUsuariosPorRol();

    // Reporte: porcentaje de usuarios que pertenece a cada rol
    @Query(value = """
            SELECT r.denominacion,
                   COUNT(u.id_usuario) AS cantidad,
                   COALESCE(ROUND(COUNT(u.id_usuario) * 100.0
                       / NULLIF((SELECT COUNT(*) FROM usuarios), 0), 2), 0) AS porcentaje
            FROM roles r
            LEFT JOIN usuarios u ON u.id_rol = r.id_rol
            GROUP BY r.denominacion
            ORDER BY cantidad DESC
            """, nativeQuery = true)
    List<Object[]> porcentajeUsuariosPorRol();
}
