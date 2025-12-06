package com.backend.app.controller;

import com.backend.app.model.Viaje;
import com.backend.app.service.ViajeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // GET /api/viajes - Solo usuarios autenticados
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Viaje>> listarTodos() {
        return ResponseEntity.ok(viajeService.listarTodos());
    }

    // GET /api/viajes/{id} - Solo usuarios autenticados
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Viaje> obtenerPorId(@PathVariable Long id) {
        return viajeService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/viajes/busqueda?origen=Lima&destino=Chimbote&fechaIda=2025-12-13
     * - origen y destino son obligatorios
     * - fechaIda es opcional; si no viene, se usa hoy
     */
    @GetMapping("/busqueda")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Viaje>> buscarPorRuta(
            @RequestParam String origen,
            @RequestParam String destino,
            @RequestParam(name = "fechaIda", required = false) String fechaIdaStr
    ) {
        LocalDate fechaIda = null;

        // El input date del front manda formato ISO: yyyy-MM-dd
        if (fechaIdaStr != null && !fechaIdaStr.isBlank()) {
            fechaIda = LocalDate.parse(fechaIdaStr);
        }

        return ResponseEntity.ok(
                viajeService.buscarPorRuta(origen, destino, fechaIda)
        );
    }

    // POST /api/viajes - Solo ADMIN puede crear
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Viaje> crear(@Valid @RequestBody Viaje viaje) {
        Viaje creado = viajeService.crear(viaje);
        return ResponseEntity.ok(creado);
    }

    // PUT /api/viajes/{id} - Solo ADMIN puede actualizar
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Viaje> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Viaje viaje) {

        Viaje actualizado = viajeService.actualizar(id, viaje);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    // DELETE /api/viajes/{id} - Solo ADMIN puede eliminar
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        viajeService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint público (no requiere token, útil para pruebas)
    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("API de Transportes Interprovincial Miranda operativa");
    }
}
