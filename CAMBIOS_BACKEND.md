# Cambios al backend para que funcione el front completo

**Nuevos endpoints**
- `POST /api/auth/registro`: registro público como TUTOR o ESTUDIANTE (crea usuario + perfil).
- `POST /api/auth/login` ahora también devuelve `idUsuario`, `nombre` y `roles`.
- `GET /api/usuarios` (listar) y `GET /api/usuarios/me` (usuario del JWT).
- `/api/roles`: CRUD (escritura solo ADMIN).
- `/api/tutores`: CRUD. El tutor tiene `idMaterias` (tabla nueva `tutor_materias`).
- `/api/estudiantes`: CRUD.

**Correcciones**
- CORS habilitado (`CorsConfig`): antes el navegador bloqueaba todas las peticiones del front.
- `UsuarioRepository.findTutorByNombre`: el JPQL `LOWER(u.rol)` era inválido → `LOWER(u.rol.denominacion)`.
- `/disponibilidades-horarias/tutor|dia` y `/reportes-progreso/tutor|estudiante` devolvían entidades
  (proxies de Hibernate + passwordHash expuesto). Ahora devuelven DTOs.
- `/api/materias`: GET para cualquier usuario autenticado; crear/editar/eliminar solo ADMIN.
- `/api/reportes-progreso`: ya no es solo ADMIN (el tutor los registra y el estudiante los ve).
- Rol "Administrador" o "ADMIN" → `ROLE_ADMIN`.
- Editar usuario sin contraseña mantiene la actual.
- `DataInitializer`: crea los roles ADMIN/TUTOR/ESTUDIANTE y el admin `admin@tutorconecta.com` / `admin123`.

**APIs de la propuesta**
- JavaMail (SMTP Gmail): confirmaciones de sesión/pago y recordatorio 24 h antes (`NotificacionService`).
  Define `MAIL_USERNAME` y `MAIL_PASSWORD` (contraseña de aplicación de Gmail). Sin esas variables no se envían correos.
- Auditoría: log `AUDITORIA` de pagos y cambios de estado de sesiones.
