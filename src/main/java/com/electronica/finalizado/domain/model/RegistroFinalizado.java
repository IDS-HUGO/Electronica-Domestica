package com.electronica.finalizado.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class RegistroFinalizado {
    private String id;
    private String registroTarjetaId;
    private String nombreCliente;
    private String numeroCelular;
    private String marca;
    private String modelo;
    private String problemaCambiado;
    private String tecnicoId;
    private String tecnicoNombre;
    private LocalDateTime fechaEntrega;
    private BigDecimal costoReparacion;
    private LocalDateTime fechaRegistro;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RegistroFinalizado() {
        this.id = UUID.randomUUID().toString();
        this.costoReparacion = BigDecimal.ZERO;
        this.fechaRegistro = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

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

    public String getTecnicoId() { return tecnicoId; }
    public void setTecnicoId(String tecnicoId) { this.tecnicoId = tecnicoId; }

    public String getTecnicoNombre() { return tecnicoNombre; }
    public void setTecnicoNombre(String tecnicoNombre) { this.tecnicoNombre = tecnicoNombre; }

    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDateTime fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public BigDecimal getCostoReparacion() { return costoReparacion; }
    public void setCostoReparacion(BigDecimal costoReparacion) { this.costoReparacion = costoReparacion; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

