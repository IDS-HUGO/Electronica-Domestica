package com.electronica.tarjeta.application.dto;

import javax.validation.constraints.NotBlank;

public class RegistroTarjetaRequestDto {
    @NotBlank(message = "El nombre del cliente es requerido")
    private String nombreCliente;

    @NotBlank(message = "El número de celular es requerido")
    private String numeroCelular;

    @NotBlank(message = "La marca es requerida")
    private String marca;

    @NotBlank(message = "El modelo es requerido")
    private String modelo;

    @NotBlank(message = "La descripción del problema es requerida")
    private String problemaDescrito;

    @NotBlank(message = "El ID del técnico es requerido")
    private String tecnicoId;

    @NotBlank(message = "El nombre del técnico es requerido")
    private String tecnicoNombre;

    private String fechaRegistro; // Opcional, formato: "18/octubre/2025"

    // Constructors
    public RegistroTarjetaRequestDto() {}

    // Getters y Setters
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getNumeroCelular() { return numeroCelular; }
    public void setNumeroCelular(String numeroCelular) { this.numeroCelular = numeroCelular; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getProblemaDescrito() { return problemaDescrito; }
    public void setProblemaDescrito(String problemaDescrito) { this.problemaDescrito = problemaDescrito; }

    public String getTecnicoId() { return tecnicoId; }
    public void setTecnicoId(String tecnicoId) { this.tecnicoId = tecnicoId; }

    public String getTecnicoNombre() { return tecnicoNombre; }
    public void setTecnicoNombre(String tecnicoNombre) { this.tecnicoNombre = tecnicoNombre; }

    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}