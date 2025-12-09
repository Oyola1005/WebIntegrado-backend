package com.backend.app.service;

import com.backend.app.model.Boleto;

import java.util.List;
import java.util.Optional;

public interface BoletoService {

    List<Boleto> listarTodos();

    Optional<Boleto> buscarPorId(Long id);

    // compra indicando asiento
    Boleto comprarBoleto(Long viajeId, Long pasajeroId, Integer numeroAsiento);

    Boleto comprarBoletoParaUsuarioActual(Long viajeId, Integer numeroAsiento, String emailUsuario);

    // Boletos del usuario (cliente) logueado
    List<Boleto> listarBoletosDeUsuarioActual(String emailUsuario);

    // asientos ocupados por viaje
    List<Integer> obtenerAsientosOcupadosPorViaje(Long viajeId);

    void eliminar(Long id);
}