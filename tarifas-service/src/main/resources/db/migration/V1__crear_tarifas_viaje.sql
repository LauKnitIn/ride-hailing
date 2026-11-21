CREATE TABLE tarifas_viaje (
    viaje_id UUID PRIMARY KEY,
    distancia_km DOUBLE PRECISION,
    tiempo_minutos DOUBLE PRECISION,
    tarifa DOUBLE PRECISION,
    cargo_parcial DOUBLE PRECISION
);
