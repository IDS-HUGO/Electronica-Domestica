// UBICACIÓN: src/main/java/com/electronica/tarjeta/infrastructure/controller/RegistroTarjetaController.java
package com.electronica.tarjeta.infrastructure.controller;

import com.electronica.tarjeta.application.dto.RegistroTarjetaRequestDto;
import com.electronica.tarjeta.application.dto.RegistroTarjetaResponseDto;
import com.electronica.tarjeta.application.usecase.*;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;

public class RegistroTarjetaController {
    private final CreateRegistroTarjetaUseCase createTarjetaUseCase;
    private final GetAllRegistroTarjetasUseCase getAllTarjetasUseCase;
    private final GetRegistroTarjetaByIdUseCase getTarjetaByIdUseCase;
    private final UpdateRegistroTarjetaUseCase updateTarjetaUseCase;
    private final DeleteRegistroTarjetaUseCase deleteTarjetaUseCase;

    public RegistroTarjetaController(
            CreateRegistroTarjetaUseCase createTarjetaUseCase,
            GetAllRegistroTarjetasUseCase getAllTarjetasUseCase,
            GetRegistroTarjetaByIdUseCase getTarjetaByIdUseCase,
            UpdateRegistroTarjetaUseCase updateTarjetaUseCase,
            DeleteRegistroTarjetaUseCase deleteTarjetaUseCase
    ) {
        this.createTarjetaUseCase = createTarjetaUseCase;
        this.getAllTarjetasUseCase = getAllTarjetasUseCase;
        this.getTarjetaByIdUseCase = getTarjetaByIdUseCase;
        this.updateTarjetaUseCase = updateTarjetaUseCase;
        this.deleteTarjetaUseCase = deleteTarjetaUseCase;
    }

    public void create(Context ctx) {
        try {
            RegistroTarjetaRequestDto request = ctx.bodyAsClass(RegistroTarjetaRequestDto.class);
            RegistroTarjetaResponseDto response = createTarjetaUseCase.execute(request);

            ctx.status(201).json(Map.of(
                    "success", true,
                    "message", "Registro de tarjeta creado exitosamente",
                    "data", response
            ));

        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error interno del servidor"
            ));
        }
    }

    public void getAll(Context ctx) {
        try {
            List<RegistroTarjetaResponseDto> response = getAllTarjetasUseCase.execute();

            ctx.json(Map.of(
                    "success", true,
                    "message", "Registros de tarjetas obtenidos exitosamente",
                    "data", response
            ));

        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error interno del servidor"
            ));
        }
    }

    public void getById(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            RegistroTarjetaResponseDto response = getTarjetaByIdUseCase.execute(id);

            ctx.json(Map.of(
                    "success", true,
                    "message", "Registro de tarjeta obtenido exitosamente",
                    "data", response
            ));

        } catch (IllegalArgumentException e) {
            ctx.status(404).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error interno del servidor"
            ));
        }
    }

    public void update(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            RegistroTarjetaRequestDto request = ctx.bodyAsClass(RegistroTarjetaRequestDto.class);
            RegistroTarjetaResponseDto response = updateTarjetaUseCase.execute(id, request);

            ctx.json(Map.of(
                    "success", true,
                    "message", "Registro de tarjeta actualizado exitosamente",
                    "data", response
            ));

        } catch (IllegalArgumentException e) {
            ctx.status(404).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error interno del servidor"
            ));
        }
    }

    public void delete(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            deleteTarjetaUseCase.execute(id);

            ctx.json(Map.of(
                    "success", true,
                    "message", "Registro de tarjeta eliminado exitosamente"
            ));

        } catch (IllegalArgumentException e) {
            ctx.status(404).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error interno del servidor"
            ));
        }
    }
}