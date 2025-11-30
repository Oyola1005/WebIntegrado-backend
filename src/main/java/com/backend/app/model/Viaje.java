package com.backend.app.model;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class Viaje {

    private Long id;

    @NotBlank
    private String origen;      // Ej: "Lima"

    @NotBlank
    private String destino;     // Ej: "Chimbote"

    @NotNull
    @Future
    private LocalDateTime fechaSalida;

    @NotNull
    @Future
    private LocalDateTime fechaLlegada;

    @Min(1)
    private double precio;

    @Min(1)
    private int asientosDisponibles;

    @NotBlank
    private String estado;      // PROGRAMADO, EN_RUTA, COMPLETADO, CANCELADO

    public Viaje() {
    }

    public Viaje(Long id, String origen, String destino,
                 LocalDateTime fechaSalida, LocalDateTime fechaLlegada,
                 double precio, int asientosDisponibles, String estado) {
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.fechaSalida = fechaSalida;
        this.fechaLlegada = fechaLlegada;
        this.precio = precio;
        this.asientosDisponibles = asientosDisponibles;
        this.estado = estado;
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public LocalDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDateTime fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public LocalDateTime getFechaLlegada() {
        return fechaLlegada;
    }

    public void setFechaLlegada(LocalDateTime fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getAsientosDisponibles() {
        return asientosDisponibles;
    }

    public void setAsientosDisponibles(int asientosDisponibles) {
        this.asientosDisponibles = asientosDisponibles;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
