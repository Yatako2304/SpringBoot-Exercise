package com.example.exercise;

import com.example.exercise.persistencia.AsistenciaMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class ExerciseApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExerciseApplication.class, args);
	}

	@Bean
	CommandLineRunner probar(AsistenciaMapper mapper) {
		return args -> {
			System.out.println("--- Todas ---");
			mapper.listar().forEach(System.out::println);

			System.out.println("--- Empleado 1 hoy ---");
			System.out.println(mapper.buscarPorEmpleadoYFecha(1, LocalDate.now()));
		};
	}
}