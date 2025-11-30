package com.backend.app.service;

import com.backend.app.model.Boleto;

import java.util.List;
import java.util.Optional;

public interface BoletoService {

    List<Boleto> listarTodos();

    Optional<Boleto> buscarPorId(Long id);

    Boleto comprarBoleto(Long viajeId, Long pasajeroId);

    void eliminar(Long id);
}
