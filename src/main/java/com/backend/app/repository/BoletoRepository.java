package com.backend.app.repository;

import com.backend.app.model.Boleto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoletoRepository extends JpaRepository<Boleto, Long> {

    // Boletos por pasajero (sin orden)
    List<Boleto> findByPasajeroId(Long pasajeroId);

    // Boletos por viaje
    List<Boleto> findByViajeId(Long viajeId);

    // 👇 NUEVO: Boletos por pasajero ordenados del más reciente al más antiguo
    List<Boleto> findByPasajeroIdOrderByFechaCompraDesc(Long pasajeroId);
}