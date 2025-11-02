package com.electronica.auth.application.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegisterRequestDto {
    @NotBlank(message = "El nombre completo es requerido")
    private String nombreCompleto;

    @NotBlank(message = "El correo electrónico es requerido")
    @Email(message = "El correo electrónico no es válido")
    private String correoElectronico;

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String contrasena;

    @NotBlank(message = "El tipo es requerido")
    private String tipo;

    // Constructors
    public RegisterRequestDto() {}

    public RegisterRequestDto(String nombreCompleto, String correoElectronico, String contrasena, String tipo) {
        this.nombreCompleto = nombreCompleto;
        this.correoElectronico = correoElectronico;
        this.contrasena = contrasena;
        this.tipo = tipo;
    }

    // Getters y Setters
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}