package com.backend.app.service;

import com.backend.app.model.Boleto;
import com.backend.app.model.Pasajero;
import com.backend.app.model.Viaje;
import com.backend.app.repository.BoletoRepository;
import com.backend.app.repository.ViajeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    @Transactional
    public Boleto comprarBoleto(Long viajeId, Long pasajeroId, Integer numeroAsiento) {

        if (numeroAsiento == null || numeroAsiento <= 0) {
            throw new IllegalArgumentException("El número de asiento es inválido.");
        }

        Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new IllegalArgumentException("No existe viaje con id: " + viajeId));

        pasajeroService.buscarPorId(pasajeroId)
                .orElseThrow(() -> new IllegalArgumentException("No existe pasajero con id: " + pasajeroId));

        if (viaje.getFechaSalida().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("No se puede comprar boleto: el viaje ya ocurrió.");
        }

        if (!"PROGRAMADO".equalsIgnoreCase(viaje.getEstado())) {
            throw new IllegalStateException("El viaje no está disponible para compra.");
        }

        if (viaje.getAsientosDisponibles() <= 0) {
            throw new IllegalStateException("No hay asientos disponibles.");
        }

        // 👇 Verificar que el asiento no esté ya ocupado
        boolean yaOcupado = boletoRepository
                .existsByViajeIdAndNumeroAsiento(viajeId, numeroAsiento);
        if (yaOcupado) {
            throw new IllegalStateException("El asiento " + numeroAsiento + " ya está ocupado.");
        }

        Boleto boleto = new Boleto(
                null,
                viajeId,
                pasajeroId,
                numeroAsiento,
                LocalDateTime.now(),
                viaje.getPrecio(),
                "PAGADO"
        );

        viaje.setAsientosDisponibles(viaje.getAsientosDisponibles() - 1);
        if (viaje.getAsientosDisponibles() == 0) {
            viaje.setEstado("COMPLETADO");
        }
        viajeRepository.save(viaje);

        return boletoRepository.save(boleto);
    }

    @Override
    @Transactional
    public Boleto comprarBoletoParaUsuarioActual(Long viajeId, String emailUsuario, Integer numeroAsiento) {
        Pasajero pasajero = pasajeroService.obtenerOPrepararPerfil(emailUsuario);
        return comprarBoleto(viajeId, pasajero.getId(), numeroAsiento);
    }

    @Override
    public List<Boleto> listarBoletosDeUsuarioActual(String emailUsuario) {
        Pasajero pasajero = pasajeroService.obtenerOPrepararPerfil(emailUsuario);
        return boletoRepository.findByPasajeroIdOrderByFechaCompraDesc(pasajero.getId());
    }

    @Override
    public List<Integer> obtenerAsientosOcupadosPorViaje(Long viajeId) {
        return boletoRepository.findByViajeId(viajeId)
                .stream()
                .map(Boleto::getNumeroAsiento)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        boletoRepository.deleteById(id);
    }
}