package tutorconecta.com.example.tutorconectaapi.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.math.BigDecimal;

import tutorconecta.com.example.tutorconectaapi.entities.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorRepository extends JpaRepository<Tutor, Long> {

// Reporte: ingresos de cada tutor (suma de pagos COMPLETADO de sus sesiones)
    @Query(value = """
            SELECT t.id_tutor, CONCAT(u.nombre, ' ', u.apellido) AS nombre_tutor,
                   COUNT(p.id_pago) AS sesiones_pagadas,
                   COALESCE(SUM(p.monto), 0) AS total_ingresos
            FROM tutores t
            JOIN usuarios u ON u.id_usuario = t.id_tutor
            LEFT JOIN sesiones s ON s.id_tutor = t.id_tutor
            LEFT JOIN pagos p ON p.id_sesion = s.id_sesion AND p.estado = 'COMPLETADO'
            GROUP BY t.id_tutor, u.nombre, u.apellido
            ORDER BY total_ingresos DESC
            """, nativeQuery = true)
    List<Object[]> ingresosPorTutor();

    // Tutores cuya tarifa por hora está dentro de un rango
    @Query("SELECT t FROM Tutor t WHERE t.tarifaPorHora BETWEEN :min AND :max ORDER BY t.tarifaPorHora")
    List<Tutor> buscarPorRangoTarifa(@Param("min") BigDecimal min, @Param("max") BigDecimal max);
}
