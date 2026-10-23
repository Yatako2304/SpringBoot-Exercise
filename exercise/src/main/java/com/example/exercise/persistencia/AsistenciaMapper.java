package com.example.exercise.persistencia;

import com.example.exercise.dominio.Asistencia;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Mapper
public interface AsistenciaMapper {

    @Select("SELECT * FROM asistencia ORDER BY fecha DESC, empleado_id")
    List<Asistencia> listar();

    @Select("SELECT * FROM asistencia WHERE id = #{id}")
    Asistencia buscarPorId(int id);

    @Select("SELECT * FROM asistencia WHERE empleado_id = #{empleadoId} ORDER BY fecha DESC")
    List<Asistencia> listarPorEmpleado(int empleadoId);

    @Select("SELECT * FROM asistencia WHERE empleado_id = #{empleadoId} AND fecha = #{fecha}")
    Asistencia buscarPorEmpleadoYFecha(@Param("empleadoId") int empleadoId,
                                       @Param("fecha") LocalDate fecha);

    @Insert("INSERT INTO asistencia (empleado_id, fecha, estado, hora_entrada, minutos_retraso) " +
            "VALUES (#{empleadoId}, #{fecha}, #{estado}, #{horaEntrada}, #{minutosRetraso})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int registrarEntrada(Asistencia a);

    @Update("UPDATE asistencia SET hora_salida = #{horaSalida} WHERE id = #{id}")
    int registrarSalida(@Param("id") int id,
                        @Param("horaSalida") LocalTime horaSalida);

    @Delete("DELETE FROM asistencia WHERE id = #{id}")
    int eliminar(int id);
}