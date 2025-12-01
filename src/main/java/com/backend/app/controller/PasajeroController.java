package com.backend.app.controller;

import com.backend.app.model.Pasajero;
import com.backend.app.service.PasajeroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pasajeros")
public class PasajeroController {

    private final PasajeroService pasajeroService;

    public PasajeroController(PasajeroService pasajeroService) {
        this.pasajeroService = pasajeroService;
    }

    // GET /api/pasajeros - Solo usuarios autenticados
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Pasajero>> listarTodos() {
        return ResponseEntity.ok(pasajeroService.listarTodos());
    }

    // GET /api/pasajeros/{id} - Solo usuarios autenticados
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Pasajero> obtenerPorId(@PathVariable Long id) {
        return pasajeroService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/pasajeros - Registro público (NO TOKEN)
    @PostMapping
    public ResponseEntity<Pasajero> crear(@Valid @RequestBody Pasajero pasajero) {
        return ResponseEntity.ok(pasajeroService.crear(pasajero));
    }

    // PUT /api/pasajeros/{id} - Solo ADMIN puede actualizar
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

    // DELETE /api/pasajeros/{id} - Solo ADMIN puede eliminar
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pasajeroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}