// UBICACIÓN: src/main/java/com/electronica/finalizado/application/dto/RegistroFinalizadoResponseDto.java
package com.electronica.finalizado.application.dto;

import java.math.BigDecimal;

public class RegistroFinalizadoResponseDto {
    private String id;
    private String registroTarjetaId;
    private String nombreCliente;
    private String numeroCelular;
    private String marca;
    private String modelo;
    private String problemaCambiado;
    private String tecnicoNombre;
    private String fechaEntrega;
    private BigDecimal costoReparacion;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    public String getTecnicoNombre() { return tecnicoNombre; }
    public void setTecnicoNombre(String tecnicoNombre) { this.tecnicoNombre = tecnicoNombre; }

    public String getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(String fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public BigDecimal getCostoReparacion() { return costoReparacion; }
    public void setCostoReparacion(BigDecimal costoReparacion) { this.costoReparacion = costoReparacion; }
}