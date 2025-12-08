// src/main/java/com/backend/app/controller/ViajeController.java
package com.backend.app.controller;

import com.backend.app.model.Viaje;
import com.backend.app.service.ViajeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/viajes")
public class ViajeController {

    private final ViajeService viajeService;

    public ViajeController(ViajeService viajeService) {
        this.viajeService = viajeService;
    }

    // ========== LISTAR ==========
    @GetMapping
    public ResponseEntity<List<Viaje>> listarTodos() {
        return ResponseEntity.ok(viajeService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Viaje> obtenerPorId(@PathVariable Long id) {
        return viajeService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ========== BUSCAR POR RUTA ==========
    @GetMapping("/busqueda")
    public ResponseEntity<List<Viaje>> buscarPorRuta(
            @RequestParam String origen,
            @RequestParam String destino,
            @RequestParam(name = "fechaIda", required = false) String fechaIdaStr
    ) {
        LocalDate fechaIda = null;

        if (fechaIdaStr != null && !fechaIdaStr.isBlank()) {
            fechaIda = LocalDate.parse(fechaIdaStr); // yyyy-MM-dd
        }

        return ResponseEntity.ok(
                viajeService.buscarPorRuta(origen, destino, fechaIda)
        );
    }

    // ========== CREAR / ACTUALIZAR / ELIMINAR ==========
    @PostMapping
    public ResponseEntity<Viaje> crear(@Valid @RequestBody Viaje viaje) {
        Viaje creado = viajeService.crear(viaje);
        return ResponseEntity.ok(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Viaje> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Viaje viaje
    ) {
        Viaje actualizado = viajeService.actualizar(id, viaje);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        viajeService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Público para pruebas
    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("API de Transportes Interprovincial Miranda operativa");
    }
}
