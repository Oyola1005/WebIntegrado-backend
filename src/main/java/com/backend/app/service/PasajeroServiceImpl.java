package com.backend.app.service;

import com.backend.app.model.Pasajero;
import com.backend.app.repository.PasajeroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PasajeroServiceImpl implements PasajeroService {

    private final PasajeroRepository pasajeroRepository;

    public PasajeroServiceImpl(PasajeroRepository pasajeroRepository) {
        this.pasajeroRepository = pasajeroRepository;
        inicializarDatosEjemplo();
    }

    private void inicializarDatosEjemplo() {
        if (pasajeroRepository.count() == 0) {
            Pasajero p1 = new Pasajero(
                    null, "Juan", "Pérez", "12345678",
                    "juan@example.com", "987654321"
            );
            Pasajero p2 = new Pasajero(
                    null, "María", "García", "87654321",
                    "maria@example.com", "912345678"
            );
            pasajeroRepository.save(p1);
            pasajeroRepository.save(p2);
        }
    }

    @Override
    public List<Pasajero> listarTodos() {
        return pasajeroRepository.findAll();
    }

    @Override
    public Optional<Pasajero> buscarPorId(Long id) {
        return pasajeroRepository.findById(id);
    }

    @Override
    public Optional<Pasajero> buscarPorEmail(String email) {   // 👈 NUEVO
        return pasajeroRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public Pasajero crear(Pasajero pasajero) {
        return pasajeroRepository.save(pasajero);
    }

    @Override
    @Transactional
    public Pasajero actualizar(Long id, Pasajero pasajero) {
        return pasajeroRepository.findById(id)
                .map(existente -> {
                    existente.setNombres(pasajero.getNombres());
                    existente.setApellidos(pasajero.getApellidos());
                    existente.setDni(pasajero.getDni());
                    existente.setEmail(pasajero.getEmail());
                    existente.setTelefono(pasajero.getTelefono());
                    return pasajeroRepository.save(existente);
                })
                .orElse(null);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        pasajeroRepository.deleteById(id);
    }
}