package com.backend.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "boletos")
public class Boleto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "viaje_id", nullable = false)
    private Long viajeId;

    @NotNull
    @Column(name = "pasajero_id", nullable = false)
    private Long pasajeroId;

    @NotNull
    @Column(name = "fecha_compra", nullable = false)
    private LocalDateTime fechaCompra;

    @Min(0)
    @Column(name = "monto_total", nullable = false)
    private double montoTotal;

    @NotNull
    @Column(nullable = false, length = 20)
    private String estado; // PAGADO, CANCELADO

    public Boleto() {
    }

    public Boleto(Long id, Long viajeId, Long pasajeroId,
                  LocalDateTime fechaCompra, double montoTotal, String estado) {
        this.id = id;
        this.viajeId = viajeId;
        this.pasajeroId = pasajeroId;
        this.fechaCompra = fechaCompra;
        this.montoTotal = montoTotal;
        this.estado = estado;
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getViajeId() {
        return viajeId;
    }

    public void setViajeId(Long viajeId) {
        this.viajeId = viajeId;
    }

    public Long getPasajeroId() {
        return pasajeroId;
    }

    public void setPasajeroId(Long pasajeroId) {
        this.pasajeroId = pasajeroId;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
