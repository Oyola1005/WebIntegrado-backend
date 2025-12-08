// src/main/java/com/backend/app/service/PasajeroServiceImpl.java
package com.backend.app.service;

import com.backend.app.dto.ActualizarPerfilRequest;
import com.backend.app.model.Pasajero;
import com.backend.app.repository.PasajeroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class PasajeroServiceImpl implements PasajeroService {

    private final PasajeroRepository pasajeroRepository;
    private final Random random = new Random();

    public PasajeroServiceImpl(PasajeroRepository pasajeroRepository) {
        this.pasajeroRepository = pasajeroRepository;
        inicializarDatosEjemplo();
    }

    // ----------- SOLO PARA DEMO / DATOS DE PRUEBA -----------
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
    // ---------------------------------------------------------

    @Override
    public List<Pasajero> listarTodos() {
        return pasajeroRepository.findAll();
    }

    @Override
    public Optional<Pasajero> buscarPorId(Long id) {
        return pasajeroRepository.findById(id);
    }

    @Override
    public Optional<Pasajero> buscarPorEmail(String email) {
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

    // ========= MÉTODOS CLAVE PARA PERFIL / CLIENTE =========

    /**
     * Genera un DNI aleatorio de 8 dígitos que no exista en la base de datos.
     */
    private String generarDniUnico() {
        String dni;
        do {
            int num = random.nextInt(100_000_000); // 0..99,999,999
            dni = String.format("%08d", num);
        } while (pasajeroRepository.findByDni(dni).isPresent());
        return dni;
    }

    /**
     * Si existe pasajero con ese email, lo devuelve.
     * Si no existe, crea uno nuevo con DNI único y datos vacíos.
     */
    @Override
    @Transactional
    public Pasajero obtenerOPrepararPerfil(String emailUsuario) {
        return pasajeroRepository.findByEmail(emailUsuario)
                .orElseGet(() -> {
                    Pasajero nuevo = new Pasajero();
                    nuevo.setNombres("");
                    nuevo.setApellidos("");
                    nuevo.setDni(generarDniUnico());  // 👈 DNI SIEMPRE ÚNICO
                    nuevo.setTelefono("");
                    nuevo.setEmail(emailUsuario);
                    return pasajeroRepository.save(nuevo);
                });
    }

    @Override
    @Transactional
    public Pasajero actualizarPerfil(String emailUsuario, ActualizarPerfilRequest request) {
        Pasajero existente = obtenerOPrepararPerfil(emailUsuario);
        existente.setNombres(request.getNombres());
        existente.setApellidos(request.getApellidos());
        existente.setTelefono(request.getTelefono());
        // NO tocamos dni ni email
        return pasajeroRepository.save(existente);
    }
}
