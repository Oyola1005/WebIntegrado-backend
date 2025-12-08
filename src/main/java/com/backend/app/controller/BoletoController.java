package com.backend.app.controller;

import com.backend.app.dto.CompraBoletoRequest;
import com.backend.app.model.Boleto;
import com.backend.app.service.BoletoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/boletos")
public class BoletoController {

    private final BoletoService boletoService;

    public BoletoController(BoletoService boletoService) {
        this.boletoService = boletoService;
    }

    // ===== ADMIN =====

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Boleto>> listarTodos() {
        return ResponseEntity.ok(boletoService.listarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boleto> obtenerPorId(@PathVariable Long id) {
        return boletoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boletoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ===== NUEVO: ASIENTOS OCUPADOS POR VIAJE =====

    @GetMapping("/ocupados/{viajeId}")
    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMIN')")
    public ResponseEntity<List<Integer>> asientosOcupados(@PathVariable Long viajeId) {
        List<Integer> ocupados = boletoService.obtenerAsientosOcupadosPorViaje(viajeId);
        return ResponseEntity.ok(ocupados);
    }

    // ===== CLIENTE: MIS BOLETOS / COMPRAR =====

    @GetMapping("/mis-boletos")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<Boleto>> listarMisBoletos(Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.emptyList());
        }

        String emailUsuario = auth.getName();

        try {
            List<Boleto> misBoletos = boletoService.listarBoletosDeUsuarioActual(emailUsuario);
            return ResponseEntity.ok(misBoletos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @PostMapping("/comprar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> comprar(@Valid @RequestBody CompraBoletoRequest request,
                                     Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String emailUsuario = auth.getName();

        try {
            Boleto boleto = boletoService.comprarBoletoParaUsuarioActual(
                    request.getViajeId(),
                    emailUsuario,
                    request.getNumeroAsiento()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(boleto);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pudo completar la compra.");
        }
    }
}