INSERT INTO horario (descripcion) VALUES ('Oficina lunes a viernes');

INSERT INTO jornada (horario_id, dia_semana, hora_inicio, hora_fin, hora_inicio_descanso, hora_fin_descanso)
SELECT 1, d, '09:00', '18:00', '13:00', '14:00'
FROM generate_series(1, 5) AS d;

INSERT INTO empleado (nombre, apellido, cargo, horario_id) VALUES
                                                               ('Juan', 'Pérez', 'Desarrollador', 1),
                                                               ('Ana', 'Torres', 'Analista', 1);

INSERT INTO asistencia (empleado_id, fecha, estado, hora_entrada, hora_salida, minutos_retraso) VALUES
                                                                                                    (1, CURRENT_DATE, 'presente', '08:55', '18:02', 0),
                                                                                                    (2, CURRENT_DATE, 'tardanza', '09:20', '18:10', 20);