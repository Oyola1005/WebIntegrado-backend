package com.backend.app.controller;

import com.backend.app.dto.CompraBoletoRequest;
import com.backend.app.model.Boleto;
import com.backend.app.service.BoletoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boletos")
public class BoletoController {

    private final BoletoService boletoService;

    public BoletoController(BoletoService boletoService) {
        this.boletoService = boletoService;
    }

    // Listar todos los boletos
    @GetMapping
    public ResponseEntity<List<Boleto>> listarTodos() {
        return ResponseEntity.ok(boletoService.listarTodos());
    }

    // Obtener boleto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Boleto> obtenerPorId(@PathVariable Long id) {
        return boletoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Comprar boleto (flujo principal)
    @PostMapping("/compra")
    public ResponseEntity<?> comprar(@Valid @RequestBody CompraBoletoRequest request) {
        try {
            Boleto boleto = boletoService.comprarBoleto(
                    request.getViajeId(),
                    request.getPasajeroId()
            );
            return ResponseEntity.ok(boleto);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Errores de negocio → 400
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Otros errores (BD, etc.) → 500 con mensaje detallado
            e.printStackTrace(); // se verá en la consola
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + e.getMessage());
        }
    }

    // Eliminar boleto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boletoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}