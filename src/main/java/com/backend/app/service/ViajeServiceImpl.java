package com.backend.app.service;

import com.backend.app.model.Viaje;
import com.backend.app.repository.ViajeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ViajeServiceImpl implements ViajeService {

    private final ViajeRepository viajeRepository;

    public ViajeServiceImpl(ViajeRepository viajeRepository) {
        this.viajeRepository = viajeRepository;
        inicializarDatosDeEjemplo();
    }

    // Carga de datos de ejemplo SOLO si la tabla está vacía
    private void inicializarDatosDeEjemplo() {
        if (viajeRepository.count() == 0) {
            Viaje v1 = new Viaje(
                    null,
                    "Lima",
                    "Chimbote",
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(1).plusHours(8),
                    80.0,
                    40,
                    "PROGRAMADO"
            );

            Viaje v2 = new Viaje(
                    null,
                    "Chimbote",
                    "Lima",
                    LocalDateTime.now().plusDays(2),
                    LocalDateTime.now().plusDays(2).plusHours(8),
                    85.0,
                    38,
                    "PROGRAMADO"
            );

            viajeRepository.save(v1);
            viajeRepository.save(v2);
        }
    }

    @Override
    public List<Viaje> listarTodos() {
        return viajeRepository.findAll();
    }

    @Override
    public Optional<Viaje> buscarPorId(Long id) {
        return viajeRepository.findById(id);
    }

    @Override
    @Transactional
    public Viaje crear(Viaje viaje) {
        // id null → JPA genera el ID
        return viajeRepository.save(viaje);
    }

    @Override
    @Transactional
    public Viaje actualizar(Long id, Viaje viajeActualizado) {
        return viajeRepository.findById(id)
                .map(existente -> {
                    existente.setOrigen(viajeActualizado.getOrigen());
                    existente.setDestino(viajeActualizado.getDestino());
                    existente.setFechaSalida(viajeActualizado.getFechaSalida());
                    existente.setFechaLlegada(viajeActualizado.getFechaLlegada());
                    existente.setPrecio(viajeActualizado.getPrecio());
                    existente.setAsientosDisponibles(viajeActualizado.getAsientosDisponibles());
                    existente.setEstado(viajeActualizado.getEstado());
                    return viajeRepository.save(existente);
                })
                .orElse(null);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        viajeRepository.deleteById(id);
    }

    @Override
    public List<Viaje> buscarPorRuta(String origen, String destino) {
        // Solo viajes FUTUROS, con asientos, PROGRAMADOS
        LocalDateTime hoy = LocalDateTime.now();
        return viajeRepository.buscarDisponiblesPorRuta(origen, destino, hoy);
    }
}