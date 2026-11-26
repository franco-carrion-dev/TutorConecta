package tutorconecta.com.example.tutorconectaapi.repositories;

import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.math.BigDecimal;

import tutorconecta.com.example.tutorconectaapi.entities.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

// Reporte: gasto total de cada estudiante (pagos COMPLETADO de sus sesiones)
    @Query(value = """
            SELECT e.id_estudiante, CONCAT(u.nombre, ' ', u.apellido) AS nombre_estudiante,
                   COUNT(p.id_pago) AS sesiones_pagadas,
                   COALESCE(SUM(p.monto), 0) AS total_gastado
            FROM estudiantes e
            JOIN usuarios u ON u.id_usuario = e.id_estudiante
            LEFT JOIN sesiones s ON s.id_estudiante = e.id_estudiante
            LEFT JOIN pagos p ON p.id_sesion = s.id_sesion AND p.estado = 'COMPLETADO'
            GROUP BY e.id_estudiante, u.nombre, u.apellido
            ORDER BY total_gastado DESC
            """, nativeQuery = true)
    List<Object[]> gastoPorEstudiante();

    // Query simple: estudiantes con presupuesto máximo mayor o igual a un monto
    List<Estudiante> findByPresupuestoMaximoGreaterThanEqualOrderByPresupuestoMaximoDesc(BigDecimal monto);
}
