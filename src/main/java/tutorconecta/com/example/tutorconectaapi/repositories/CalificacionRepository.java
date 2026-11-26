package tutorconecta.com.example.tutorconectaapi.repositories;

import tutorconecta.com.example.tutorconectaapi.entities.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {

    @Query("""
            SELECT c
            FROM Calificacion c
            JOIN FETCH c.tutor t
            WHERE t.idTutor = :idTutor
            ORDER BY c.fechaCalificacion DESC
            """)
    List<Calificacion> findCalificacionesByTutorId(@Param("idTutor") Long idTutor);

// Reporte: promedio de puntuación y cantidad de calificaciones por tutor
    @Query(value = """
            SELECT t.id_tutor, CONCAT(u.nombre, ' ', u.apellido) AS nombre_tutor,
                   COUNT(c.id_calificacion) AS cantidad,
                   COALESCE(ROUND(AVG(c.puntuacion), 2), 0) AS promedio
            FROM tutores t
            JOIN usuarios u ON u.id_usuario = t.id_tutor
            LEFT JOIN calificaciones c ON c.id_tutor = t.id_tutor
            GROUP BY t.id_tutor, u.nombre, u.apellido
            ORDER BY promedio DESC, cantidad DESC
            """, nativeQuery = true)
    List<Object[]> promedioPorTutor();

    // Reporte: cuántas calificaciones hay de cada puntuación (1 a 5, incluye las que tienen 0)
    @Query(value = """
            SELECT g.puntuacion, COUNT(c.id_calificacion) AS cantidad
            FROM generate_series(1, 5) AS g(puntuacion)
            LEFT JOIN calificaciones c ON c.puntuacion = g.puntuacion
            GROUP BY g.puntuacion
            ORDER BY g.puntuacion DESC
            """, nativeQuery = true)
    List<Object[]> contarPorPuntuacion();
}
