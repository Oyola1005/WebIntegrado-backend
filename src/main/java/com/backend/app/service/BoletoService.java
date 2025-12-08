package com.backend.app.service;

import com.backend.app.model.Boleto;

import java.util.List;
import java.util.Optional;

public interface BoletoService {

    List<Boleto> listarTodos();

    Optional<Boleto> buscarPorId(Long id);

    Boleto comprarBoleto(Long viajeId, Long pasajeroId, Integer numeroAsiento);

    Boleto comprarBoletoParaUsuarioActual(Long viajeId, String emailUsuario, Integer numeroAsiento);

    // Boletos del usuario (cliente) logueado
    List<Boleto> listarBoletosDeUsuarioActual(String emailUsuario);

    // NUEVO: números de asiento ocupados para un viaje
    List<Integer> obtenerAsientosOcupadosPorViaje(Long viajeId);

    void eliminar(Long id);
}