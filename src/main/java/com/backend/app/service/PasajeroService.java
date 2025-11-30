package com.backend.app.service;

import com.backend.app.model.Pasajero;

import java.util.List;
import java.util.Optional;

public interface PasajeroService {

    List<Pasajero> listarTodos();

    Optional<Pasajero> buscarPorId(Long id);

    Pasajero crear(Pasajero pasajero);

    Pasajero actualizar(Long id, Pasajero pasajero);

    void eliminar(Long id);
}