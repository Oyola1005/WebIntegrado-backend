package com.backend.app.dto;

import jakarta.validation.constraints.NotNull;

public class CompraBoletoRequest {

    @NotNull(message = "El id del viaje es obligatorio")
    private Long viajeId;

    @NotNull(message = "El id del pasajero es obligatorio")
    private Long pasajeroId;

    public CompraBoletoRequest() {
    }

    public CompraBoletoRequest(Long viajeId, Long pasajeroId) {
        this.viajeId = viajeId;
        this.pasajeroId = pasajeroId;
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
}
