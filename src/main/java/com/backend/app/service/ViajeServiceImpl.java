package com.backend.app.service;

import com.backend.app.model.Viaje;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ViajeServiceImpl implements ViajeService {

    private final Map<Long, Viaje> almacenamiento = new ConcurrentHashMap<>();
    private final AtomicLong secuenciaId = new AtomicLong(0);

    public ViajeServiceImpl() {
        // Datos de ejemplo: Lima -> Chimbote y Chimbote -> Lima
        Viaje v1 = new Viaje(
                secuenciaId.incrementAndGet(),
                "Lima",
                "Chimbote",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(8),
                80.0,
                40,
                "PROGRAMADO"
        );

        Viaje v2 = new Viaje(
                secuenciaId.incrementAndGet(),
                "Chimbote",
                "Lima",
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(2).plusHours(8),
                85.0,
                38,
                "PROGRAMADO"
        );

        almacenamiento.put(v1.getId(), v1);
        almacenamiento.put(v2.getId(), v2);
    }

    @Override
    public List<Viaje> listarTodos() {
        return new ArrayList<>(almacenamiento.values());
    }

    @Override
    public Optional<Viaje> buscarPorId(Long id) {
        return Optional.ofNullable(almacenamiento.get(id));
    }

    @Override
    public Viaje crear(Viaje viaje) {
        Long nuevoId = secuenciaId.incrementAndGet();
        viaje.setId(nuevoId);
        almacenamiento.put(nuevoId, viaje);
        return viaje;
    }

    @Override
    public Viaje actualizar(Long id, Viaje viajeActualizado) {
        Viaje existente = almacenamiento.get(id);
        if (existente == null) {
            return null;
        }
        viajeActualizado.setId(id);
        almacenamiento.put(id, viajeActualizado);
        return viajeActualizado;
    }

    @Override
    public void eliminar(Long id) {
        almacenamiento.remove(id);
    }

    @Override
    public List<Viaje> buscarPorRuta(String origen, String destino) {
        return almacenamiento.values().stream()
                .filter(v -> v.getOrigen().equalsIgnoreCase(origen)
                          && v.getDestino().equalsIgnoreCase(destino))
                .collect(Collectors.toList());
    }
}