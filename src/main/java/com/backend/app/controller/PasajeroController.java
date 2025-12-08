package com.backend.app.controller;

import com.backend.app.dto.ActualizarPerfilRequest;
import com.backend.app.model.Pasajero;
import com.backend.app.service.PasajeroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pasajeros")
public class PasajeroController {

    private final PasajeroService pasajeroService;

    public PasajeroController(PasajeroService pasajeroService) {
        this.pasajeroService = pasajeroService;
    }

    // ===== CRUD GENERAL (ADMIN) =====

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Pasajero>> listarTodos() {
        return ResponseEntity.ok(pasajeroService.listarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Pasajero> obtenerPorId(@PathVariable Long id) {
        return pasajeroService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pasajero> crear(@Valid @RequestBody Pasajero pasajero) {
        return ResponseEntity.ok(pasajeroService.crear(pasajero));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Pasajero> actualizar(@PathVariable Long id,
                                               @Valid @RequestBody Pasajero pasajero) {
        Pasajero actualizado = pasajeroService.actualizar(id, pasajero);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pasajeroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ===== PERFIL DEL USUARIO LOGUEADO (CLIENTE / CUALQUIER AUTENTICADO) =====

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Pasajero> miPerfil(Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            // por si algo raro pasa con el filtro
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String emailUsuario = auth.getName();
        Pasajero perfil = pasajeroService.obtenerOPrepararPerfil(emailUsuario);
        return ResponseEntity.ok(perfil);
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Pasajero> actualizarMiPerfil(
            @Valid @RequestBody ActualizarPerfilRequest datos,
            Authentication auth
    ) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String emailUsuario = auth.getName();
        Pasajero actualizado = pasajeroService.actualizarPerfil(emailUsuario, datos);
        return ResponseEntity.ok(actualizado);
    }
}
