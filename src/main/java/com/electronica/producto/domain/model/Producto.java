package com.electronica.producto.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Producto {
    private String id;
    private String nombreProducto;
    private String categoria;
    private BigDecimal cantidadOhms;
    private String unidad;
    private Integer cantidadPiezas;
    private BigDecimal precioUnitario;
    private LocalDateTime fechaRegistro;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Producto() {
        this.id = UUID.randomUUID().toString();
        this.cantidadPiezas = 0;
        this.precioUnitario = BigDecimal.ZERO;
        this.fechaRegistro = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public BigDecimal getCantidadOhms() { return cantidadOhms; }
    public void setCantidadOhms(BigDecimal cantidadOhms) { this.cantidadOhms = cantidadOhms; }

    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }

    public Integer getCantidadPiezas() { return cantidadPiezas; }
    public void setCantidadPiezas(Integer cantidadPiezas) { this.cantidadPiezas = cantidadPiezas; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}