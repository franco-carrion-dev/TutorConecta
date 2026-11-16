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
}