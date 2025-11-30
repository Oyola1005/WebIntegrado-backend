package com.backend.app.controller;

import com.backend.app.model.Viaje;
import com.backend.app.service.ViajeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/viajes")
public class ViajeController {

    private final ViajeService viajeService;

    public ViajeController(ViajeService viajeService) {
        this.viajeService = viajeService;
    }

    // GET /api/viajes
    @GetMapping
    public ResponseEntity<List<Viaje>> listarTodos() {
        return ResponseEntity.ok(viajeService.listarTodos());
    }

    // GET /api/viajes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Viaje> obtenerPorId(@PathVariable Long id) {
        return viajeService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/viajes/busqueda?origen=Lima&destino=Chimbote
    @GetMapping("/busqueda")
    public ResponseEntity<List<Viaje>> buscarPorRuta(
            @RequestParam String origen,
            @RequestParam String destino
    ) {
        return ResponseEntity.ok(viajeService.buscarPorRuta(origen, destino));
    }

    // POST /api/viajes
    @PostMapping
    public ResponseEntity<Viaje> crear(@Valid @RequestBody Viaje viaje) {
        Viaje creado = viajeService.crear(viaje);
        return ResponseEntity.ok(creado);
    }

    // PUT /api/viajes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Viaje> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Viaje viaje) {

        Viaje actualizado = viajeService.actualizar(id, viaje);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    // DELETE /api/viajes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        viajeService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint simple para ver estado del backend
    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("API de Transportes Interprovincial Miranda operativa");
    }
}