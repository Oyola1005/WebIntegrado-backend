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

import java.util.List;

@RestController
@RequestMapping("/api/boletos")
public class BoletoController {

    private final BoletoService boletoService;

    public BoletoController(BoletoService boletoService) {
        this.boletoService = boletoService;
    }

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

    // 👇 NUEVO: boletos del usuario logueado (CLIENTE)
    @GetMapping("/mis-boletos")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> listarMisBoletos(Authentication auth) {
        try {
            String emailUsuario = auth.getName();  // viene del JWT
            List<Boleto> misBoletos = boletoService.listarBoletosDeUsuarioActual(emailUsuario);
            return ResponseEntity.ok(misBoletos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + e.getMessage());
        }
    }

    @PostMapping("/comprar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> comprar(@Valid @RequestBody CompraBoletoRequest request,
                                     Authentication auth) {
        try {
            String emailUsuario = auth.getName();  // 👈 viene del JWT

            Boleto boleto = boletoService.comprarBoletoParaUsuarioActual(
                    request.getViajeId(),
                    emailUsuario
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(boleto);

        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boletoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}