package com.backend.app.service;

import com.backend.app.model.Boleto;
import com.backend.app.model.Viaje;
import com.backend.app.repository.BoletoRepository;
import com.backend.app.repository.ViajeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BoletoServiceImpl implements BoletoService {

    private final BoletoRepository boletoRepository;
    private final ViajeRepository viajeRepository;
    private final PasajeroService pasajeroService;

    public BoletoServiceImpl(BoletoRepository boletoRepository,
                             ViajeRepository viajeRepository,
                             PasajeroService pasajeroService) {
        this.boletoRepository = boletoRepository;
        this.viajeRepository = viajeRepository;
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

    /**
     * Compra de boleto transaccional:
     * - Verifica que exista el viaje
     * - Verifica que exista el pasajero
     * - Verifica asientos disponibles y estado del viaje
     * - Crea el boleto
     * - Descuenta 1 asiento del viaje
     * Si algo falla, se hace rollback automático.
     */
    @Override
    @Transactional
    public Boleto comprarBoleto(Long viajeId, Long pasajeroId) {

        // 1) Validar viaje existente
        Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() ->
                        new IllegalArgumentException("No existe viaje con id: " + viajeId));

        // 2) Validar pasajero existente
        pasajeroService.buscarPorId(pasajeroId)
                .orElseThrow(() ->
                        new IllegalArgumentException("No existe pasajero con id: " + pasajeroId));

        // 3) Validar estado del viaje
        if (!"PROGRAMADO".equalsIgnoreCase(viaje.getEstado())) {
            throw new IllegalStateException(
                    "El viaje no está disponible para compra. Estado actual: " + viaje.getEstado()
            );
        }

        // 4) Validar asientos disponibles
        if (viaje.getAsientosDisponibles() <= 0) {
            throw new IllegalStateException("No hay asientos disponibles para este viaje");
        }

        // 5) Crear boleto
        Boleto boleto = new Boleto(
                null,
                viajeId,
                pasajeroId,
                LocalDateTime.now(),
                viaje.getPrecio(),
                "PAGADO"
        );

        // 6) Actualizar asientos del viaje
        int nuevosAsientos = viaje.getAsientosDisponibles() - 1;
        viaje.setAsientosDisponibles(nuevosAsientos);

        // Opcional: si ya no hay asientos, marcar viaje como COMPLETADO
        if (nuevosAsientos == 0) {
            viaje.setEstado("COMPLETADO");
        }

        viajeRepository.save(viaje);

        // 7) Guardar boleto
        return boletoRepository.save(boleto);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        boletoRepository.deleteById(id);
    }
}