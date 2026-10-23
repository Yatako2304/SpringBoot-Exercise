-- CREATES
CREATE TABLE horario (
                         id          SERIAL PRIMARY KEY,
                         descripcion VARCHAR(100) NOT NULL
);

CREATE TABLE jornada (
                         id                   SERIAL PRIMARY KEY,
                         horario_id           INTEGER NOT NULL REFERENCES horario(id),
                         dia_semana           INTEGER NOT NULL,
                         hora_inicio          TIME NOT NULL,
                         hora_fin             TIME NOT NULL,
                         hora_inicio_descanso TIME,
                         hora_fin_descanso    TIME,
                         UNIQUE (horario_id, dia_semana)
);

CREATE TABLE empleado (
                          id         SERIAL PRIMARY KEY,
                          nombre     VARCHAR(100) NOT NULL,
                          apellido   VARCHAR(100) NOT NULL,
                          cargo      VARCHAR(100),
                          horario_id INTEGER NOT NULL REFERENCES horario(id)
);

CREATE TABLE asistencia (
                            id                   SERIAL PRIMARY KEY,
                            empleado_id          INTEGER NOT NULL REFERENCES empleado(id),
                            fecha                DATE NOT NULL,
                            estado               VARCHAR(20) NOT NULL DEFAULT 'presente',
                            hora_entrada         TIME,
                            hora_inicio_descanso TIME,
                            hora_fin_descanso    TIME,
                            hora_salida          TIME,
                            minutos_retraso      INTEGER DEFAULT 0,
                            UNIQUE (empleado_id, fecha)
);
