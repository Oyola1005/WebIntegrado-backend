package com.backend.app.service;

import com.backend.app.model.Pasajero;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PasajeroServiceImpl implements PasajeroService {

    private final Map<Long, Pasajero> almacenamiento = new ConcurrentHashMap<>();
    private final AtomicLong secuenciaId = new AtomicLong(0);

    public PasajeroServiceImpl() {
        // Pasajeros de ejemplo
        Pasajero p1 = new Pasajero(secuenciaId.incrementAndGet(),
                "Juan", "Pérez", "12345678", "juan@example.com", "987654321");
        Pasajero p2 = new Pasajero(secuenciaId.incrementAndGet(),
                "María", "García", "87654321", "maria@example.com", "912345678");

        almacenamiento.put(p1.getId(), p1);
        almacenamiento.put(p2.getId(), p2);
    }

    @Override
    public List<Pasajero> listarTodos() {
        return new ArrayList<>(almacenamiento.values());
    }

    @Override
    public Optional<Pasajero> buscarPorId(Long id) {
        return Optional.ofNullable(almacenamiento.get(id));
    }

    @Override
    public Pasajero crear(Pasajero pasajero) {
        Long nuevoId = secuenciaId.incrementAndGet();
        pasajero.setId(nuevoId);
        almacenamiento.put(nuevoId, pasajero);
        return pasajero;
    }

    @Override
    public Pasajero actualizar(Long id, Pasajero pasajero) {
        if (!almacenamiento.containsKey(id)) {
            return null;
        }
        pasajero.setId(id);
        almacenamiento.put(id, pasajero);
        return pasajero;
    }

    @Override
    public void eliminar(Long id) {
        almacenamiento.remove(id);
    }
}
