package com.backend.app.service;

import com.backend.app.model.Viaje;

import java.util.List;
import java.util.Optional;

public interface ViajeService {

    List<Viaje> listarTodos();

    Optional<Viaje> buscarPorId(Long id);

    Viaje crear(Viaje viaje);

    Viaje actualizar(Long id, Viaje viajeActualizado);

    void eliminar(Long id);

    List<Viaje> buscarPorRuta(String origen, String destino);
}