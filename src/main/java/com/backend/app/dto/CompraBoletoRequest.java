package com.backend.app.dto;

import jakarta.validation.constraints.NotNull;

public class CompraBoletoRequest {

    @NotNull(message = "El id del viaje es obligatorio")
    private Long viajeId;

    @NotNull(message = "El número de asiento es obligatorio")
    private Integer numeroAsiento;

    public CompraBoletoRequest() {
    }

    public CompraBoletoRequest(Long viajeId, Integer numeroAsiento) {
        this.viajeId = viajeId;
        this.numeroAsiento = numeroAsiento;
    }

    public Long getViajeId() {
        return viajeId;
    }

    public void setViajeId(Long viajeId) {
        this.viajeId = viajeId;
    }

    public Integer getNumeroAsiento() {
        return numeroAsiento;
    }

    public void setNumeroAsiento(Integer numeroAsiento) {
        this.numeroAsiento = numeroAsiento;
    }
}