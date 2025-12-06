package com.backend.app.repository;

import com.backend.app.model.Pasajero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasajeroRepository extends JpaRepository<Pasajero, Long> {

    Optional<Pasajero> findByDni(String dni);

    Optional<Pasajero> findByEmail(String email);
}