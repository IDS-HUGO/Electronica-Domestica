package com.electronica.tarjeta.application.usecase;

import com.electronica.tarjeta.application.dto.RegistroTarjetaResponseDto;
import com.electronica.tarjeta.domain.model.RegistroTarjeta;
import com.electronica.tarjeta.domain.port.RegistroTarjetaRepositoryPort;

import java.time.format.DateTimeFormatter;

public class GetRegistroTarjetaByIdUseCase {
    private final RegistroTarjetaRepositoryPort repository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MMMM/yyyy");

    public GetRegistroTarjetaByIdUseCase(RegistroTarjetaRepositoryPort repository) {
        this.repository = repository;
    }

    public RegistroTarjetaResponseDto execute(String id) {
        RegistroTarjeta tarjeta = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro de tarjeta no encontrado"));

        return mapToResponseDto(tarjeta);
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