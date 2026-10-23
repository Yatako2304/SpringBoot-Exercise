package com.example.exercise.servicio;

import com.example.exercise.dominio.Asistencia;
import com.example.exercise.excepcion.RecursoNoEncontradoException;
import com.example.exercise.excepcion.ReglaNegocioException;
import com.example.exercise.persistencia.AsistenciaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsistenciaServiceTest {

    @Mock
    AsistenciaMapper mapper;

    @InjectMocks
    AsistenciaService service;

    // Lunes 26 de octubre de 2026
    static final LocalDate LUNES = LocalDate.of(2026, 10, 26);
    static final LocalDate SABADO = LocalDate.of(2026, 10, 24);

    @BeforeEach
    void empleadoExiste() {
        lenient().when(mapper.existeEmpleado(1)).thenReturn(true);
    }

    @Test
    void registrarEntrada_aTiempo_quedaPresente() {
        when(mapper.buscarHoraInicioJornada(1, 1)).thenReturn(LocalTime.of(9, 0));

        Asistencia a = service.registrarEntrada(1, LUNES.atTime(8, 55));

        assertEquals("presente", a.getEstado());
        assertEquals(0, a.getMinutosRetraso());
        verify(mapper).registrarEntrada(a);
    }

    @Test
    void registrarEntrada_tarde_quedaTardanzaConMinutos() {
        when(mapper.buscarHoraInicioJornada(1, 1)).thenReturn(LocalTime.of(9, 0));

        Asistencia a = service.registrarEntrada(1, LUNES.atTime(9, 15));

        assertEquals("tardanza", a.getEstado());
        assertEquals(15, a.getMinutosRetraso());
    }

    @Test
    void registrarEntrada_dosVecesElMismoDia_lanzaReglaNegocio() {
        when(mapper.buscarPorEmpleadoYFecha(1, LUNES)).thenReturn(new Asistencia());

        assertThrows(ReglaNegocioException.class,
                () -> service.registrarEntrada(1, LUNES.atTime(9, 0)));

        verify(mapper, never()).registrarEntrada(any());
    }

    @Test
    void registrarEntrada_sinJornadaEseDia_lanzaReglaNegocio() {
        when(mapper.buscarHoraInicioJornada(1, 6)).thenReturn(null);   // 6 = sábado

        ReglaNegocioException e = assertThrows(ReglaNegocioException.class,
                () -> service.registrarEntrada(1, SABADO.atTime(9, 0)));

        assertEquals("El empleado no tiene jornada este día", e.getMessage());
    }

    @Test
    void registrarEntrada_empleadoInexistente_lanzaNoEncontrado() {
        when(mapper.existeEmpleado(999)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.registrarEntrada(999, LUNES.atTime(9, 0)));
    }

    @Test
    void registrarSalida_sinEntrada_lanzaReglaNegocio() {
        when(mapper.buscarPorEmpleadoYFecha(1, LUNES)).thenReturn(null);

        assertThrows(ReglaNegocioException.class,
                () -> service.registrarSalida(1, LUNES.atTime(18, 0)));
    }

    @Test
    void registrarSalida_conEntrada_guardaLaHora() {
        Asistencia existente = new Asistencia();
        existente.setId(10);
        when(mapper.buscarPorEmpleadoYFecha(1, LUNES)).thenReturn(existente);

        Asistencia a = service.registrarSalida(1, LUNES.atTime(18, 5));

        assertEquals(LocalTime.of(18, 5), a.getHoraSalida());
        verify(mapper).registrarSalida(10, LocalTime.of(18, 5));
    }
}