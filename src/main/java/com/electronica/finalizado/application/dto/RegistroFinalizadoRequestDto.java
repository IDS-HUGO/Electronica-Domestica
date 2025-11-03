// --- DTOs ---
// UBICACIÓN: src/main/java/com/electronica/finalizado/application/dto/RegistroFinalizadoRequestDto.java
package com.electronica.finalizado.application.dto;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class RegistroFinalizadoRequestDto {
    @NotBlank(message = "El ID de registro de tarjeta es requerido")
    private String registroTarjetaId;

    @NotBlank(message = "El nombre del cliente es requerido")
    private String nombreCliente;

    @NotBlank(message = "El número de celular es requerido")
    private String numeroCelular;

    @NotBlank(message = "La marca es requerida")
    private String marca;

    @NotBlank(message = "El modelo es requerido")
    private String modelo;

    @NotBlank(message = "La descripción de lo cambiado es requerida")
    private String problemaCambiado;

    @NotBlank(message = "El ID del técnico es requerido")
    private String tecnicoId;

    @NotBlank(message = "El nombre del técnico es requerido")
    private String tecnicoNombre;

    @NotBlank(message = "La fecha de entrega es requerida")
    private String fechaEntrega;

    private BigDecimal costoReparacion;

    // Getters y Setters
    public String getRegistroTarjetaId() { return registroTarjetaId; }
    public void setRegistroTarjetaId(String registroTarjetaId) { this.registroTarjetaId = registroTarjetaId; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getNumeroCelular() { return numeroCelular; }
    public void setNumeroCelular(String numeroCelular) { this.numeroCelular = numeroCelular; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getProblemaCambiado() { return problemaCambiado; }
    public void setProblemaCambiado(String problemaCambiado) { this.problemaCambiado = problemaCambiado; }

    public String getTecnicoId() { return tecnicoId; }
    public void setTecnicoId(String tecnicoId) { this.tecnicoId = tecnicoId; }

    public String getTecnicoNombre() { return tecnicoNombre; }
    public void setTecnicoNombre(String tecnicoNombre) { this.tecnicoNombre = tecnicoNombre; }

    public String getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(String fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public BigDecimal getCostoReparacion() { return costoReparacion; }
    public void setCostoReparacion(BigDecimal costoReparacion) { this.costoReparacion = costoReparacion; }
}