// UBICACIÓN: src/main/java/com/electronica/tarjeta/application/usecase/UpdateRegistroTarjetaUseCase.java
package com.electronica.tarjeta.application.usecase;

import com.electronica.tarjeta.application.dto.RegistroTarjetaRequestDto;
import com.electronica.tarjeta.application.dto.RegistroTarjetaResponseDto;
import com.electronica.tarjeta.domain.model.RegistroTarjeta;
import com.electronica.tarjeta.domain.port.RegistroTarjetaRepositoryPort;

import java.time.format.DateTimeFormatter;

public class UpdateRegistroTarjetaUseCase {
    private final RegistroTarjetaRepositoryPort repository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MMMM/yyyy");

    public UpdateRegistroTarjetaUseCase(RegistroTarjetaRepositoryPort repository) {
        this.repository = repository;
    }

    public RegistroTarjetaResponseDto execute(String id, RegistroTarjetaRequestDto request) {
        // Verificar que el registro existe
        RegistroTarjeta existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro de tarjeta no encontrado"));

        // Actualizar campos
        existing.setNombreCliente(request.getNombreCliente());
        existing.setNumeroCelular(request.getNumeroCelular());
        existing.setMarca(request.getMarca());
        existing.setModelo(request.getModelo());
        existing.setProblemaDescrito(request.getProblemaDescrito());

        // Actualizar en BD
        RegistroTarjeta updated = repository.update(id, existing);

        return mapToResponseDto(updated);
    }

    private RegistroTarjetaResponseDto mapToResponseDto(RegistroTarjeta tarjeta) {
        RegistroTarjetaResponseDto dto = new RegistroTarjetaResponseDto();
        dto.setId(tarjeta.getId());
        dto.setNombreCliente(tarjeta.getNombreCliente());
        dto.setNumeroCelular(tarjeta.getNumeroCelular());
        dto.setMarca(tarjeta.getMarca());
        dto.setModelo(tarjeta.getModelo());
        dto.setProblemaDescrito(tarjeta.getProblemaDescrito());
        dto.setTecnicoId(tarjeta.getTecnicoId());
        dto.setTecnicoNombre(tarjeta.getTecnicoNombre());
        dto.setEstado(tarjeta.getEstado());
        dto.setFechaRegistro(tarjeta.getFechaRegistro().format(FORMATTER));

        if (tarjeta.getFechaFinalizacion() != null) {
            dto.setFechaFinalizacion(tarjeta.getFechaFinalizacion().format(FORMATTER));
        }

        if (tarjeta.getFechaEntrega() != null) {
            dto.setFechaEntrega(tarjeta.getFechaEntrega().format(FORMATTER));
        }

        return dto;
    }
}