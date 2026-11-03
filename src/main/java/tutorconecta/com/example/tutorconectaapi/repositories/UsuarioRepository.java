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
            WHERE LOWER(u.rol) = 'tutor' 
            AND LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))
            """)
    List<Usuario> findTutorByNombre(@Param("nombre") String nombre);

    // Query nativa: reporte de cantidad de usuarios (total y activos) por rol
    @Query(value = """
            SELECT u.id_rol, COUNT(u.id_usuario) AS total_usuarios,
                   SUM(CASE WHEN u.activo = TRUE THEN 1 ELSE 0 END) AS usuarios_activos
            FROM usuarios u
            GROUP BY u.id_rol
            ORDER BY total_usuarios DESC
            """, nativeQuery = true)
    List<Object[]> reporteUsuariosPorRol();
}