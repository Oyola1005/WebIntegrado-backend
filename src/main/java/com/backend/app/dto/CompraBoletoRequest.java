package com.backend.app.dto;

import jakarta.validation.constraints.NotNull;

public class CompraBoletoRequest {

    @NotNull(message = "El id del viaje es obligatorio")
    private Long viajeId;

    public CompraBoletoRequest() {}

    public CompraBoletoRequest(Long viajeId) {
        this.viajeId = viajeId;
    }

    public Long getViajeId() {
        return viajeId;
    }

    public void setViajeId(Long viajeId) {
        this.viajeId = viajeId;
    }
}