package com.example.exercise.controlador;

import com.example.exercise.dominio.Asistencia;
import com.example.exercise.servicio.AsistenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Asistencia> buscar(@PathVariable int id) {
        Asistencia a = service.buscarPorId(id);
        return a != null ? ResponseEntity.ok(a) : ResponseEntity.notFound().build();
    }

    @GetMapping("/empleado/{empleadoId}")
    public List<Asistencia> listarPorEmpleado(@PathVariable int empleadoId) {
        return service.listarPorEmpleado(empleadoId);
    }

    @PostMapping("/empleado/{empleadoId}/entrada")
    public ResponseEntity<Asistencia> registrarEntrada(@PathVariable int empleadoId) {
        Asistencia a = service.registrarEntrada(empleadoId, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(a);
    }

    @PostMapping("/empleado/{empleadoId}/salida")
    public Asistencia registrarSalida(@PathVariable int empleadoId) {
        return service.registrarSalida(empleadoId, LocalDateTime.now());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        return service.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> reglaIncumplida(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", e.getMessage()));
    }
}