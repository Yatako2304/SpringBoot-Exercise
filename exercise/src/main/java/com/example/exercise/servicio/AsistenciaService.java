package com.example.exercise.servicio;

import com.example.exercise.dominio.Asistencia;
import com.example.exercise.excepcion.RecursoNoEncontradoException;
import com.example.exercise.excepcion.ReglaNegocioException;
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
        Asistencia a = mapper.buscarPorId(id);
        if (a == null) {
            throw new RecursoNoEncontradoException("No existe la asistencia con id " + id);
        }
        return a;
    }

    public List<Asistencia> listarPorEmpleado(int empleadoId) {
        validarEmpleado(empleadoId);
        return mapper.listarPorEmpleado(empleadoId);
    }

    @Transactional
    public Asistencia registrarEntrada(int empleadoId, LocalDateTime momento) {
        validarEmpleado(empleadoId);

        LocalDate fecha = momento.toLocalDate();
        LocalTime hora = momento.toLocalTime();

        if (mapper.buscarPorEmpleadoYFecha(empleadoId, fecha) != null) {
            throw new ReglaNegocioException("El empleado ya registró su entrada hoy");
        }

        int diaSemana = fecha.getDayOfWeek().getValue();
        LocalTime horaInicio = mapper.buscarHoraInicioJornada(empleadoId, diaSemana);
        if (horaInicio == null) {
            throw new ReglaNegocioException("El empleado no tiene jornada este día");
        }

        int retraso = (int) Math.max(0, Duration.between(horaInicio, hora).toMinutes());

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
        validarEmpleado(empleadoId);

        Asistencia a = mapper.buscarPorEmpleadoYFecha(empleadoId, momento.toLocalDate());
        if (a == null) {
            throw new ReglaNegocioException("El empleado no registró entrada hoy");
        }
        if (a.getHoraSalida() != null) {
            throw new ReglaNegocioException("El empleado ya registró su salida hoy");
        }

        mapper.registrarSalida(a.getId(), momento.toLocalTime());
        a.setHoraSalida(momento.toLocalTime());
        return a;
    }

    public void eliminar(int id) {
        if (mapper.eliminar(id) == 0) {
            throw new RecursoNoEncontradoException("No existe la asistencia con id " + id);
        }
    }

    private void validarEmpleado(int empleadoId) {
        if (!mapper.existeEmpleado(empleadoId)) {
            throw new RecursoNoEncontradoException("No existe el empleado con id " + empleadoId);
        }
    }
}