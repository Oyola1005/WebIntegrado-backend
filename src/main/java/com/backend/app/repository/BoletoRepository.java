package com.backend.app.repository;

import com.backend.app.model.Boleto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoletoRepository extends JpaRepository<Boleto, Long> {

    List<Boleto> findByPasajeroId(Long pasajeroId);

    List<Boleto> findByViajeId(Long viajeId);
}