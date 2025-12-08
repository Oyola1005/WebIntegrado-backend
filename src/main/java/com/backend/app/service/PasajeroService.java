package com.backend.app.service;

import com.backend.app.dto.ActualizarPerfilRequest;
import com.backend.app.model.Pasajero;

import java.util.List;
import java.util.Optional;

public interface PasajeroService {

    List<Pasajero> listarTodos();

    Optional<Pasajero> buscarPorId(Long id);

    Optional<Pasajero> buscarPorEmail(String email);

    Pasajero crear(Pasajero pasajero);

    Pasajero actualizar(Long id, Pasajero pasajero);

    void eliminar(Long id);

    // Actualizar perfil usando el email del usuario logueado
    Pasajero actualizarPerfil(String emailUsuario, ActualizarPerfilRequest request);

    // 🔹 Garantiza que SIEMPRE haya un pasajero para ese email
    Pasajero obtenerOPrepararPerfil(String emailUsuario);
}
