package com.backend.app.repository;

import com.backend.app.model.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ViajeRepository extends JpaRepository<Viaje, Long> {

    // Ejemplo de JPQL para la rúbrica (búsqueda por ruta)
    @Query("SELECT v FROM Viaje v " +
           "WHERE LOWER(v.origen) = LOWER(:origen) " +
           "AND LOWER(v.destino) = LOWER(:destino)")
    List<Viaje> buscarPorRuta(String origen, String destino);
}