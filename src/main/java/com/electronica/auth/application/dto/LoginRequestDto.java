package com.electronica.auth.application.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class LoginRequestDto {
    @NotBlank(message = "El correo electrónico es requerido")
    @Email(message = "El correo electrónico no es válido")
    private String correoElectronico;

    @NotBlank(message = "La contraseña es requerida")
    private String contrasena;

    // Constructors
    public LoginRequestDto() {}

    public LoginRequestDto(String correoElectronico, String contrasena) {
        this.correoElectronico = correoElectronico;
        this.contrasena = contrasena;
    }

    // Getters y Setters
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
}