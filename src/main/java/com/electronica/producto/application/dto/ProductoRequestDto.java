package com.electronica.producto.application.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductoRequestDto {
    @NotBlank(message = "El nombre del producto es requerido")
    private String nombreProducto;

    @NotBlank(message = "La categoría es requerida")
    private String categoria;

    private BigDecimal cantidadOhms; // Opcional
    private String unidad; // Opcional

    @NotNull(message = "La cantidad de piezas es requerida")
    private Integer cantidadPiezas;

    private BigDecimal precioUnitario;
    private String fechaRegistro;

    // Getters y Setters
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

    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}