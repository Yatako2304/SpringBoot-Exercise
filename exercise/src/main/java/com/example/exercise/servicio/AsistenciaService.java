package com.example.exercise.servicio;

import com.example.exercise.dominio.Asistencia;
import com.example.exercise.persistencia.AsistenciaMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AsistenciaService {

    private final AsistenciaMapper mapper;

    public AsistenciaService(AsistenciaMapper mapper) {
        this.mapper = mapper;
    }

    public List<Asistencia> listar() {
        return mapper.listar();
    }

    public Asistencia buscarPorId(int id) {
        return mapper.buscarPorId(id);
    }

    public List<Asistencia> listarPorEmpleado(int empleadoId) {
        return mapper.listarPorEmpleado(empleadoId);
    }

    @Transactional
    public Asistencia registrarEntrada(int empleadoId, LocalDateTime momento) {
        LocalDate fecha = momento.toLocalDate();
        LocalTime hora = momento.toLocalTime();

        // una entrada por día
        if (mapper.buscarPorEmpleadoYFecha(empleadoId, fecha) != null) {
            throw new IllegalStateException("El empleado ya registró su entrada hoy");
        }

        // debe tener jornada ese dia (1 = lunes ... 7 = domingo)
        int diaSemana = fecha.getDayOfWeek().getValue();
        LocalTime horaInicio = mapper.buscarHoraInicioJornada(empleadoId, diaSemana);
        if (horaInicio == null) {
            throw new IllegalStateException("El empleado no tiene jornada este día");
        }

        // alcular retraso
        long minutos = Duration.between(horaInicio, hora).toMinutes();
        int retraso = (int) Math.max(0, minutos);

        Asistencia a = new Asistencia();
        a.setEmpleadoId(empleadoId);
        a.setFecha(fecha);
        a.setHoraEntrada(hora);
        a.setMinutosRetraso(retraso);
        a.setEstado(retraso > 0 ? "tardanza" : "presente");

        mapper.registrarEntrada(a);
        return a;
    }

    @Transactional
    public Asistencia registrarSalida(int empleadoId, LocalDateTime momento) {
        Asistencia a = mapper.buscarPorEmpleadoYFecha(empleadoId, momento.toLocalDate());

        // Regla 4: debe haber marcado entrada y no haber marcado salida
        if (a == null) {
            throw new IllegalStateException("El empleado no registró entrada hoy");
        }
        if (a.getHoraSalida() != null) {
            throw new IllegalStateException("El empleado ya registró su salida hoy");
        }

        mapper.registrarSalida(a.getId(), momento.toLocalTime());
        a.setHoraSalida(momento.toLocalTime());
        return a;
    }

    public boolean eliminar(int id) {
        return mapper.eliminar(id) > 0;
    }
}