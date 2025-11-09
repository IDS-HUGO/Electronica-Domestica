package com.electronica.finalizado.services;

import com.electronica.finalizado.models.RegistroFinalizado;
import com.electronica.finalizado.repositories.FinalizadoRepository;
import io.javalin.http.Context;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class FinalizadoService {
    private final FinalizadoRepository repository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MMMM/yyyy");

    public FinalizadoService(FinalizadoRepository repository) {
        this.repository = repository;
    }

    public void create(io.javalin.http.Context ctx) {
        try {
            Map<String, Object> request = ctx.bodyAsClass(Map.class);
            Map<String, Object> response = createFinalizado(request);
            ctx.json(response);
            ctx.status(201);
        } catch (IllegalArgumentException e) {
            ctx.status(400);
            ctx.json(java.util.Map.of("error", e.getMessage()));
        }
    }

    private Map<String, Object> createFinalizado(Map<String, Object> request) {
        // Validaciones
        String registroTarjetaId = (String) request.get("registroTarjetaId");
        String nombreCliente = (String) request.get("nombreCliente");
        String numeroCelular = (String) request.get("numeroCelular");
        String marca = (String) request.get("marca");
        String modelo = (String) request.get("modelo");
        String problemaCambiado = (String) request.get("problemaCambiado");
        String tecnicoId = (String) request.get("tecnicoId");
        String tecnicoNombre = (String) request.get("tecnicoNombre");
        String fechaEntrega = (String) request.get("fechaEntrega");

        if (registroTarjetaId == null || registroTarjetaId.isEmpty()) {
            throw new IllegalArgumentException("El ID de registro de tarjeta es requerido");
        }
        if (nombreCliente == null || nombreCliente.isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es requerido");
        }
        if (numeroCelular == null || numeroCelular.isEmpty()) {
            throw new IllegalArgumentException("El número de celular es requerido");
        }
        if (marca == null || marca.isEmpty()) {
            throw new IllegalArgumentException("La marca es requerida");
        }
        if (modelo == null || modelo.isEmpty()) {
            throw new IllegalArgumentException("El modelo es requerido");
        }
        if (problemaCambiado == null || problemaCambiado.isEmpty()) {
            throw new IllegalArgumentException("La descripción de lo cambiado es requerida");
        }
        if (tecnicoId == null || tecnicoId.isEmpty()) {
            throw new IllegalArgumentException("El ID del técnico es requerido");
        }
        if (tecnicoNombre == null || tecnicoNombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre del técnico es requerido");
        }
        if (fechaEntrega == null || fechaEntrega.isEmpty()) {
            throw new IllegalArgumentException("La fecha de entrega es requerida");
        }

        // Crear entidad
        RegistroFinalizado finalizado = new RegistroFinalizado();
        finalizado.setId(UUID.randomUUID().toString());
        finalizado.setRegistroTarjetaId(registroTarjetaId);
        finalizado.setNombreCliente(nombreCliente);
        finalizado.setNumeroCelular(numeroCelular);
        finalizado.setMarca(marca);
        finalizado.setModelo(modelo);
        finalizado.setProblemaCambiado(problemaCambiado);
        finalizado.setTecnicoId(tecnicoId);
        finalizado.setTecnicoNombre(tecnicoNombre);
        finalizado.setFechaEntrega(parseFecha(fechaEntrega));
        finalizado.setCostoReparacion(getBigDecimal(request, "costoReparacion"));
        finalizado.setFechaRegistro(LocalDateTime.now());
        finalizado.setCreatedAt(LocalDateTime.now());
        finalizado.setUpdatedAt(LocalDateTime.now());

        // Guardar
        RegistroFinalizado saved = repository.save(finalizado);

        return toMap(saved);
    }

    public void getAll(io.javalin.http.Context ctx) {
        List<Map<String, Object>> response = repository.findAll().stream()
                .map(this::toMap)
                .collect(Collectors.toList());
        ctx.json(response);
    }

    public void getById(io.javalin.http.Context ctx) {
        try {
            String id = ctx.pathParam("id");
            RegistroFinalizado finalizado = repository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Registro finalizado no encontrado"));
            ctx.json(toMap(finalizado));
        } catch (IllegalArgumentException e) {
            ctx.status(404);
            ctx.json(java.util.Map.of("error", e.getMessage()));
        }
    }

    public void update(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            Map<String, Object> request = ctx.bodyAsClass(Map.class);
            // Verificar que existe
            RegistroFinalizado existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro finalizado no encontrado"));

            // Actualizar campos
            if (request.containsKey("problemaCambiado")) {
                existing.setProblemaCambiado((String) request.get("problemaCambiado"));
            }
            if (request.containsKey("fechaEntrega")) {
                existing.setFechaEntrega(parseFecha((String) request.get("fechaEntrega")));
            }
            if (request.containsKey("costoReparacion")) {
                existing.setCostoReparacion(getBigDecimal(request, "costoReparacion"));
            }

            existing.setUpdatedAt(LocalDateTime.now());

            // Guardar
            RegistroFinalizado updated = repository.update(id, existing);
            ctx.json(toMap(updated));
        } catch (IllegalArgumentException e) {
            ctx.status(404);
            ctx.json(Map.of("error", e.getMessage()));
        }
    }

    public void delete(io.javalin.http.Context ctx) {
        try {
            String id = ctx.pathParam("id");
            // Verificar que existe
            repository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Registro finalizado no encontrado"));

            repository.deleteById(id);
            ctx.status(204);
        } catch (IllegalArgumentException e) {
            ctx.status(404);
            ctx.json(java.util.Map.of("error", e.getMessage()));
        }
    }

    public void getByTecnico(io.javalin.http.Context ctx) {
        String tecnicoId = ctx.pathParam("tecnicoId");
        List<Map<String, Object>> result = repository.findByTecnicoId(tecnicoId).stream()
                .map(this::toMap)
                .collect(Collectors.toList());
        ctx.json(result);
    }

    private Map<String, Object> toMap(RegistroFinalizado finalizado) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", finalizado.getId());
        map.put("registroTarjetaId", finalizado.getRegistroTarjetaId());
        map.put("nombreCliente", finalizado.getNombreCliente());
        map.put("numeroCelular", finalizado.getNumeroCelular());
        map.put("marca", finalizado.getMarca());
        map.put("modelo", finalizado.getModelo());
        map.put("problemaCambiado", finalizado.getProblemaCambiado());
        map.put("tecnicoNombre", finalizado.getTecnicoNombre());
        map.put("fechaEntrega", finalizado.getFechaEntrega().format(FORMATTER));
        map.put("costoReparacion", finalizado.getCostoReparacion());
        return map;
    }

    private LocalDateTime parseFecha(String fecha) {
        try {
            return LocalDateTime.parse(fecha + " 00:00:00",
                    DateTimeFormatter.ofPattern("dd/MMMM/yyyy HH:mm:ss"));
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }

    private BigDecimal getBigDecimal(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return BigDecimal.ZERO;
        if (value instanceof BigDecimal) return (BigDecimal) value;
        if (value instanceof Number) return BigDecimal.valueOf(((Number) value).doubleValue());
        if (value instanceof String) return new BigDecimal((String) value);
        return BigDecimal.ZERO;
    }
}