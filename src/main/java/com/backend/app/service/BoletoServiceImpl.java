package com.backend.app.service;

import com.backend.app.model.Boleto;
import com.backend.app.model.Viaje;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BoletoServiceImpl implements BoletoService {

    private final Map<Long, Boleto> almacenamiento = new ConcurrentHashMap<>();
    private final AtomicLong secuenciaId = new AtomicLong(0);

    private final ViajeService viajeService;
    private final PasajeroService pasajeroService;

    public BoletoServiceImpl(ViajeService viajeService, PasajeroService pasajeroService) {
        this.viajeService = viajeService;
        this.pasajeroService = pasajeroService;
    }

    @Override
    public List<Boleto> listarTodos() {
        return new ArrayList<>(almacenamiento.values());
    }

    @Override
    public Optional<Boleto> buscarPorId(Long id) {
        return Optional.ofNullable(almacenamiento.get(id));
    }

    @Override
    public Boleto comprarBoleto(Long viajeId, Long pasajeroId) {

        Viaje viaje = viajeService.buscarPorId(viajeId)
                .orElseThrow(() -> new IllegalArgumentException("Viaje no encontrado"));

        pasajeroService.buscarPorId(pasajeroId)
                .orElseThrow(() -> new IllegalArgumentException("Pasajero no encontrado"));

        if (viaje.getAsientosDisponibles() <= 0) {
            throw new IllegalStateException("No hay asientos disponibles para este viaje");
        }

        // Descontar 1 asiento del viaje
        viaje.setAsientosDisponibles(viaje.getAsientosDisponibles() - 1);
        viajeService.actualizar(viaje.getId(), viaje);

        // Crear boleto
        Long nuevoId = secuenciaId.incrementAndGet();
        Boleto boleto = new Boleto(
                nuevoId,
                viajeId,
                pasajeroId,
                LocalDateTime.now(),
                viaje.getPrecio(),
                "PAGADO"
        );

        almacenamiento.put(nuevoId, boleto);
        return boleto;
    }

    @Override
    public void eliminar(Long id) {
        almacenamiento.remove(id);
    }
}
