package tutorconecta.com.example.tutorconectaapi.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tutorconecta.com.example.tutorconectaapi.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Encuentra un usuario por su email
    Usuario findByEmail(String email);

    @Query("""
            SELECT u
            FROM Usuario u
            WHERE LOWER(u.rol.denominacion) = 'tutor'
            AND LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))
            """)
    List<Usuario> findTutorByNombre(@Param("nombre") String nombre);

// Reporte: cantidad de usuarios registrados por mes en un año
    @Query(value = """
            SELECT CAST(EXTRACT(MONTH FROM u.fecha_registro) AS INTEGER) AS mes,
                   COUNT(u.id_usuario) AS cantidad
            FROM usuarios u
            WHERE EXTRACT(YEAR FROM u.fecha_registro) = :anio
            GROUP BY mes
            ORDER BY mes
            """, nativeQuery = true)
    List<Object[]> contarRegistrosPorMes(@Param("anio") int anio);

    // Query simple: usuarios activos o inactivos, del más reciente al más antiguo
    List<Usuario> findByActivoOrderByFechaRegistroDesc(Boolean activo);
}
