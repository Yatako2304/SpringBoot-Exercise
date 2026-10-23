package com.example.exercise.controlador;

import com.example.exercise.dominio.Asistencia;
import com.example.exercise.excepcion.RecursoNoEncontradoException;
import com.example.exercise.excepcion.ReglaNegocioException;
import com.example.exercise.servicio.AsistenciaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AsistenciaController.class)
class AsistenciaControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    AsistenciaService service;

    @Test
    void buscar_existente_devuelve200ConJson() throws Exception {
        Asistencia a = new Asistencia();
        a.setId(1);
        a.setEmpleadoId(1);
        a.setFecha(LocalDate.of(2026, 10, 26));
        a.setEstado("presente");
        when(service.buscarPorId(1)).thenReturn(a);

        mvc.perform(get("/asistencias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("presente"))
                .andExpect(jsonPath("$.fecha").value("2026-10-26"));
    }

    @Test
    void buscar_inexistente_devuelve404() throws Exception {
        when(service.buscarPorId(999))
                .thenThrow(new RecursoNoEncontradoException("No existe la asistencia con id 999"));

        mvc.perform(get("/asistencias/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("No existe la asistencia con id 999"));
    }

    @Test
    void buscar_idNoNumerico_devuelve400() throws Exception {
        mvc.perform(get("/asistencias/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscar_idNegativo_devuelve400() throws Exception {
        mvc.perform(get("/asistencias/-5"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registrarEntrada_valida_devuelve201() throws Exception {
        Asistencia a = new Asistencia();
        a.setId(5);
        a.setEstado("tardanza");
        when(service.registrarEntrada(eq(1), any(LocalDateTime.class))).thenReturn(a);

        mvc.perform(post("/asistencias/empleado/1/entrada"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void registrarEntrada_repetida_devuelve409() throws Exception {
        when(service.registrarEntrada(eq(1), any(LocalDateTime.class)))
                .thenThrow(new ReglaNegocioException("El empleado ya registró su entrada hoy"));

        mvc.perform(post("/asistencias/empleado/1/entrada"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("El empleado ya registró su entrada hoy"));
    }
}