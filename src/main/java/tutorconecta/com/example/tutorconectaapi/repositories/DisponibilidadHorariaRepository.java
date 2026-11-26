package tutorconecta.com.example.tutorconectaapi.repositories;

import tutorconecta.com.example.tutorconectaapi.entities.DisponibilidadHoraria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DisponibilidadHorariaRepository extends JpaRepository<DisponibilidadHoraria, Long> {

    @Query("""
            SELECT d
            FROM DisponibilidadHoraria d
            JOIN FETCH d.tutor t
            WHERE t.idTutor = :idTutor
            ORDER BY d.diaSemana, d.horaInicio
            """)
    List<DisponibilidadHoraria> findDisponibilidadByTutorId(@Param("idTutor") Long idTutor);
    //Buscar disponibilidad por dia
    @Query("""
            SELECT d
            FROM DisponibilidadHoraria d
            JOIN FETCH d.tutor t
            WHERE LOWER(d.diaSemana) = LOWER(:diaSemana)
            """)
    List<DisponibilidadHoraria> findByDiaSemana(@Param("diaSemana") String diaSemana);

// Reporte: bloques y horas disponibles por día de la semana (todos los tutores)
    @Query(value = """
            SELECT d.dia_semana, COUNT(d.id_disponibilidad) AS bloques,
                   ROUND(CAST(SUM(EXTRACT(EPOCH FROM (d.hora_fin - d.hora_inicio)) / 3600) AS NUMERIC), 2) AS total_horas
            FROM disponibilidades_horarias d
            WHERE d.disponible = true
            GROUP BY d.dia_semana
            ORDER BY CASE LOWER(d.dia_semana)
                WHEN 'lunes' THEN 1 WHEN 'martes' THEN 2 WHEN 'miércoles' THEN 3 WHEN 'miercoles' THEN 3
                WHEN 'jueves' THEN 4 WHEN 'viernes' THEN 5 WHEN 'sábado' THEN 6 WHEN 'sabado' THEN 6
                ELSE 7 END
            """, nativeQuery = true)
    List<Object[]> horasDisponiblesPorDia();

    // Reporte: bloques y horas disponibles por tutor a la semana
    @Query(value = """
            SELECT t.id_tutor, CONCAT(u.nombre, ' ', u.apellido) AS nombre_tutor,
                   COUNT(d.id_disponibilidad) AS bloques,
                   COALESCE(ROUND(CAST(SUM(EXTRACT(EPOCH FROM (d.hora_fin - d.hora_inicio)) / 3600) AS NUMERIC), 2), 0) AS total_horas
            FROM tutores t
            JOIN usuarios u ON u.id_usuario = t.id_tutor
            LEFT JOIN disponibilidades_horarias d ON d.id_tutor = t.id_tutor AND d.disponible = true
            GROUP BY t.id_tutor, u.nombre, u.apellido
            ORDER BY total_horas DESC
            """, nativeQuery = true)
    List<Object[]> horasDisponiblesPorTutor();
}
