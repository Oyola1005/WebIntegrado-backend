// src/main/java/com/backend/app/dto/ActualizarPerfilRequest.java
package com.backend.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ActualizarPerfilRequest {

    @NotBlank
    @Size(min = 2, max = 60)
    private String nombres;

    @NotBlank
    @Size(min = 2, max = 60)
    private String apellidos;

    // 9 dígitos O vacío (opcional)
    @Pattern(regexp = "^(\\d{9})?$",
             message = "El teléfono debe tener 9 dígitos o quedar vacío")
    private String telefono;

    public ActualizarPerfilRequest() {
    }

    public ActualizarPerfilRequest(String nombres, String apellidos, String telefono) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
