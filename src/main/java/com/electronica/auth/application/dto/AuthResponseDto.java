package com.electronica.auth.application.dto;

public class AuthResponseDto {
    private String token;
    private String userId;
    private String email;
    private String nombreCompleto;
    private String tipo;

    // Constructors
    public AuthResponseDto() {}

    public AuthResponseDto(String token, String userId, String email, String nombreCompleto, String tipo) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.nombreCompleto = nombreCompleto;
        this.tipo = tipo;
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}