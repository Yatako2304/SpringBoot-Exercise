package com.example.exercise.dominio;

import java.time.LocalDate;
import java.time.LocalTime;

public class Asistencia {
    private int id;
    private int empleadoId;
    private LocalDate fecha;
    private String estado;
    private LocalTime horaEntrada;
    private LocalTime horaInicioDescanso;
    private LocalTime horaFinDescanso;
    private LocalTime horaSalida;
    private int minutosRetraso;

    public Asistencia() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(int empleadoId) { this.empleadoId = empleadoId; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalTime getHoraEntrada() { return horaEntrada; }
    public void setHoraEntrada(LocalTime horaEntrada) { this.horaEntrada = horaEntrada; }

    public LocalTime getHoraInicioDescanso() { return horaInicioDescanso; }
    public void setHoraInicioDescanso(LocalTime horaInicioDescanso) { this.horaInicioDescanso = horaInicioDescanso; }

    public LocalTime getHoraFinDescanso() { return horaFinDescanso; }
    public void setHoraFinDescanso(LocalTime horaFinDescanso) { this.horaFinDescanso = horaFinDescanso; }

    public LocalTime getHoraSalida() { return horaSalida; }
    public void setHoraSalida(LocalTime horaSalida) { this.horaSalida = horaSalida; }

    public int getMinutosRetraso() { return minutosRetraso; }
    public void setMinutosRetraso(int minutosRetraso) { this.minutosRetraso = minutosRetraso; }

    @Override
    public String toString() {
        return "Asistencia{id=" + id +
                ", empleadoId=" + empleadoId +
                ", fecha=" + fecha +
                ", estado='" + estado + "'" +
                ", entrada=" + horaEntrada +
                ", salida=" + horaSalida +
                ", retraso=" + minutosRetraso + "}";
    }
}