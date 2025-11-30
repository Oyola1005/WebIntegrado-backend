package com.backend.app.service;

import com.backend.app.model.Boleto;
import com.backend.app.model.Viaje;
import com.backend.app.repository.BoletoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BoletoServiceImpl implements BoletoService {

    private final BoletoRepository boletoRepository;
    private final ViajeService viajeService;
    private final PasajeroService pasajeroService;

    public BoletoServiceImpl(BoletoRepository boletoRepository,
                             ViajeService viajeService,
                             PasajeroService pasajeroService) {
        this.boletoRepository = boletoRepository;
        this.viajeService = viajeService;
        this.pasajeroService = pasajeroService;
    }

    @Override
    public List<Boleto> listarTodos() {
        return boletoRepository.findAll();
    }

    @Override
    public Optional<Boleto> buscarPorId(Long id) {
        return boletoRepository.findById(id);
    }

    @Override
    @Transactional
    public Boleto comprarBoleto(Long viajeId, Long pasajeroId) {

        Viaje viaje = viajeService.buscarPorId(viajeId)
                .orElseThrow(() -> new IllegalArgumentException("Viaje no encontrado"));

        pasajeroService.buscarPorId(pasajeroId)
                .orElseThrow(() -> new IllegalArgumentException("Pasajero no encontrado"));

        if (viaje.getAsientosDisponibles() <= 0) {
            throw new IllegalStateException("No hay asientos disponibles para este viaje");
        }

        // Descontamos asiento
        viaje.setAsientosDisponibles(viaje.getAsientosDisponibles() - 1);
        viajeService.actualizar(viaje.getId(), viaje);

        // Creamos y guardamos boleto
        Boleto boleto = new Boleto(
                null,
                viajeId,
                pasajeroId,
                LocalDateTime.now(),
                viaje.getPrecio(),
                "PAGADO"
        );

        return boletoRepository.save(boleto);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        boletoRepository.deleteById(id);
    }
}
