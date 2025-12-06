package com.backend.app.dto;

public class AuthResponse {

    private String token;
    private String rol;
    private String nombreMostrado; // 👈 nombre que verá el usuario

    public AuthResponse() {
    }

    public AuthResponse(String token, String rol, String nombreMostrado) {
        this.token = token;
        this.rol = rol;
        this.nombreMostrado = nombreMostrado;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getNombreMostrado() {
        return nombreMostrado;
    }

    public void setNombreMostrado(String nombreMostrado) {
        this.nombreMostrado = nombreMostrado;
    }
}
