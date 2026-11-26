package tutorconecta.com.example.tutorconectaapi.repositories;

import tutorconecta.com.example.tutorconectaapi.entities.ReporteProgreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReporteProgresoRepository extends JpaRepository<ReporteProgreso, Long> {

    @Query("""
            SELECT r
            FROM ReporteProgreso r
            JOIN FETCH r.sesion s
            JOIN FETCH s.estudiante e
            WHERE e.idEstudiante = :idEstudiante
            ORDER BY r.fechaReporte DESC
            """)
    List<ReporteProgreso> findReportesByEstudianteId(@Param("idEstudiante") Long idEstudiante);
    // Buscar reportes por tutor
    @Query("""
            SELECT r
            FROM ReporteProgreso r
            JOIN FETCH r.sesion s
            JOIN FETCH s.tutor t
            WHERE t.idTutor = :idTutor
            ORDER BY r.fechaReporte DESC
            """)
    List<ReporteProgreso> findReportesByTutorId(@Param("idTutor") Long idTutor);

// Reporte: cantidad de reportes por nivel de progreso (Inicial, En progreso, Logrado, Destacado)
    @Query(value = """
            SELECT COALESCE(r.progreso, 'Sin nivel') AS progreso, COUNT(r.id_reporte) AS cantidad
            FROM reportes_progreso r
            GROUP BY COALESCE(r.progreso, 'Sin nivel')
            ORDER BY cantidad DESC
            """, nativeQuery = true)
    List<Object[]> contarPorNivelProgreso();

    // Reporte: cantidad de reportes de progreso emitidos por materia
    @Query(value = """
            SELECT m.id_materia, m.nombre, m.nivel, COUNT(r.id_reporte) AS cantidad
            FROM materias m
            LEFT JOIN sesiones s ON s.id_materia = m.id_materia
            LEFT JOIN reportes_progreso r ON r.id_sesion = s.id_sesion
            GROUP BY m.id_materia, m.nombre, m.nivel
            ORDER BY cantidad DESC, m.nombre
            """, nativeQuery = true)
    List<Object[]> contarPorMateria();
}
