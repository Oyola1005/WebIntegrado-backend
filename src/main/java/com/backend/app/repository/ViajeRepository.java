package com.backend.app.repository;

import com.backend.app.model.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ViajeRepository extends JpaRepository<Viaje, Long> {

    /**
     * Busca viajes disponibles por ruta:
     * - origen y destino (case-insensitive)
     * - fecha de salida HOY o futura
     * - con asientos disponibles
     * - estado PROGRAMADO
     */
    @Query("""
           SELECT v FROM Viaje v
           WHERE LOWER(v.origen) = LOWER(:origen)
             AND LOWER(v.destino) = LOWER(:destino)
             AND v.fechaSalida >= :hoy
             AND v.asientosDisponibles > 0
             AND v.estado = 'PROGRAMADO'
           ORDER BY v.fechaSalida ASC
           """)
    List<Viaje> buscarDisponiblesPorRuta(
            @Param("origen") String origen,
            @Param("destino") String destino,
            @Param("hoy") LocalDateTime hoy
    );
}
