package com.example.exercise.controlador;

import com.example.exercise.dominio.Asistencia;
import com.example.exercise.servicio.AsistenciaService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/asistencias")
public class AsistenciaController {

    private final AsistenciaService service;

    public AsistenciaController(AsistenciaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Asistencia> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Asistencia buscar(@PathVariable @Positive int id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/empleado/{empleadoId}")
    public List<Asistencia> listarPorEmpleado(@PathVariable @Positive int empleadoId) {
        return service.listarPorEmpleado(empleadoId);
    }

    @PostMapping("/empleado/{empleadoId}/entrada")
    public ResponseEntity<Asistencia> registrarEntrada(@PathVariable @Positive int empleadoId) {
        Asistencia a = service.registrarEntrada(empleadoId, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(a);
    }

    @PostMapping("/empleado/{empleadoId}/salida")
    public Asistencia registrarSalida(@PathVariable @Positive int empleadoId) {
        return service.registrarSalida(empleadoId, LocalDateTime.now());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable @Positive int id) {
        service.eliminar(id);
    }
}