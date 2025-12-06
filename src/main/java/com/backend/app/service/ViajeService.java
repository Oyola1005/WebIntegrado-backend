package com.backend.app.service;

import com.backend.app.model.Viaje;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ViajeService {

    List<Viaje> listarTodos();

    Optional<Viaje> buscarPorId(Long id);

    Viaje crear(Viaje viaje);

    Viaje actualizar(Long id, Viaje viajeActualizado);

    void eliminar(Long id);

    /**
     * Busca viajes por ruta desde una fecha mínima.
     * @param origen ciudad de origen
     * @param destino ciudad de destino
     * @param fechaSalidaMin fecha mínima (solo día). Si es null, se usa hoy.
     */
    List<Viaje> buscarPorRuta(String origen, String destino, LocalDate fechaSalidaMin);
}
