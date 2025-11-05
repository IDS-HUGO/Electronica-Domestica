package com.electronica.tarjeta.application.usecase;

import com.electronica.tarjeta.application.dto.RegistroTarjetaRequestDto;
import com.electronica.tarjeta.application.dto.RegistroTarjetaResponseDto;
import com.electronica.tarjeta.domain.model.RegistroTarjeta;
import com.electronica.tarjeta.domain.port.RegistroTarjetaRepositoryPort;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreateRegistroTarjetaUseCase {
    private final RegistroTarjetaRepositoryPort repository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MMMM/yyyy");

    public CreateRegistroTarjetaUseCase(RegistroTarjetaRepositoryPort repository) {
        this.repository = repository;
    }

    public RegistroTarjetaResponseDto execute(RegistroTarjetaRequestDto request) {
        // Crear entidad de dominio
        RegistroTarjeta tarjeta = new RegistroTarjeta();
        tarjeta.setNombreCliente(request.getNombreCliente());
        tarjeta.setNumeroCelular(request.getNumeroCelular());
        tarjeta.setMarca(request.getMarca());
        tarjeta.setModelo(request.getModelo());
        tarjeta.setProblemaDescrito(request.getProblemaDescrito());
        tarjeta.setTecnicoId(request.getTecnicoId());
        tarjeta.setTecnicoNombre(request.getTecnicoNombre());
        tarjeta.setEstado("EN_PROGRESO");

        // Si se proporciona fecha de registro, parsearla
        if (request.getFechaRegistro() != null && !request.getFechaRegistro().isEmpty()) {
            try {
                LocalDateTime fecha = LocalDateTime.parse(request.getFechaRegistro() + " 00:00:00",
                        DateTimeFormatter.ofPattern("dd/MMMM/yyyy HH:mm:ss"));
                tarjeta.setFechaRegistro(fecha);
            } catch (Exception e) {
                // Si falla el parseo, usar fecha actual
                tarjeta.setFechaRegistro(LocalDateTime.now());
            }
        }

        // Guardar en BD
        RegistroTarjeta saved = repository.save(tarjeta);

        // Convertir a DTO de respuesta
        return mapToResponseDto(saved);
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