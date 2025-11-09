package com.electronica.tarjeta.services;

import com.electronica.tarjeta.models.Tarjeta;
import com.electronica.tarjeta.repositories.TarjetaRepository;
import io.javalin.http.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TarjetaService {
    private final TarjetaRepository repository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MMMM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MMMM/yyyy HH:mm:ss");

    public TarjetaService(TarjetaRepository repository) {
        this.repository = repository;
    }

    public void create(Context ctx) {
        try {
            var body = ctx.bodyAsClass(Map.class);

            Tarjeta tarjeta = new Tarjeta();
            tarjeta.setNombreCliente((String) body.get("nombreCliente"));
            tarjeta.setNumeroCelular((String) body.get("numeroCelular"));
            tarjeta.setMarca((String) body.get("marca"));
            tarjeta.setModelo((String) body.get("modelo"));
            tarjeta.setProblemaDescrito((String) body.get("problemaDescrito"));
            tarjeta.setTecnicoId((String) body.get("tecnicoId"));
            tarjeta.setTecnicoNombre((String) body.get("tecnicoNombre"));

            Tarjeta saved = repository.save(tarjeta);

            ctx.status(201).json(Map.of(
                    "success", true,
                    "message", "Tarjeta registrada exitosamente",
                    "data", mapToResponse(saved)
            ));

        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error al crear tarjeta"
            ));
        }
    }

    public void getAll(Context ctx) {
        try {
            List<Tarjeta> tarjetas = repository.findAll();

            List<Map<String, Object>> response = tarjetas.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            ctx.json(Map.of(
                    "success", true,
                    "message", "Tarjetas obtenidas exitosamente",
                    "data", response
            ));

        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error al obtener tarjetas"
            ));
        }
    }

    public void getById(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            Tarjeta tarjeta = repository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Tarjeta no encontrada"));

            ctx.json(Map.of(
                    "success", true,
                    "message", "Tarjeta obtenida exitosamente",
                    "data", mapToResponse(tarjeta)
            ));

        } catch (IllegalArgumentException e) {
            ctx.status(404).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error al obtener tarjeta"
            ));
        }
    }

    public void update(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            var body = ctx.bodyAsClass(Map.class);

            Tarjeta existing = repository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Tarjeta no encontrada"));

            existing.setNombreCliente((String) body.get("nombreCliente"));
            existing.setNumeroCelular((String) body.get("numeroCelular"));
            existing.setMarca((String) body.get("marca"));
            existing.setModelo((String) body.get("modelo"));
            existing.setProblemaDescrito((String) body.get("problemaDescrito"));
            
            // Actualizar el técnico si se proporciona
            if (body.containsKey("tecnicoId")) {
                existing.setTecnicoId((String) body.get("tecnicoId"));
            }
            if (body.containsKey("tecnicoNombre")) {
                existing.setTecnicoNombre((String) body.get("tecnicoNombre"));
            }

            // Actualizar el estado si se proporciona
            if (body.containsKey("estado")) {
                String nuevoEstado = (String) body.get("estado");
                existing.setEstado(nuevoEstado);
                
                // Actualizar fechas según el estado
                if (nuevoEstado.equals("FINALIZADO")) {
                    existing.setFechaFinalizacion(LocalDateTime.now());
                } else if (nuevoEstado.equals("ENTREGADO")) {
                    existing.setFechaEntrega(LocalDateTime.now());
                }
            }

            existing.setUpdatedAt(LocalDateTime.now());
            Tarjeta updated = repository.update(id, existing);

            ctx.json(Map.of(
                    "success", true,
                    "message", "Tarjeta actualizada exitosamente",
                    "data", mapToResponse(updated)
            ));

        } catch (IllegalArgumentException e) {
            ctx.status(404).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error al actualizar tarjeta"
            ));
        }
    }

    public void delete(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            repository.deleteById(id);

            ctx.json(Map.of(
                    "success", true,
                    "message", "Tarjeta eliminada exitosamente"
            ));

        } catch (IllegalArgumentException e) {
            ctx.status(404).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error al eliminar tarjeta"
            ));
        }
    }

    private Map<String, Object> mapToResponse(Tarjeta tarjeta) {
        var builder = new java.util.LinkedHashMap<String, Object>();
        
        builder.put("id", tarjeta.getId());
        builder.put("nombreCliente", tarjeta.getNombreCliente());
        builder.put("numeroCelular", tarjeta.getNumeroCelular());
        builder.put("marca", tarjeta.getMarca());
        builder.put("modelo", tarjeta.getModelo());
        builder.put("problemaDescrito", tarjeta.getProblemaDescrito());
        builder.put("tecnicoId", tarjeta.getTecnicoId());
        builder.put("tecnicoNombre", tarjeta.getTecnicoNombre());
        builder.put("estado", tarjeta.getEstado());
        builder.put("fechaRegistro", tarjeta.getFechaRegistro().format(TIME_FORMATTER));
        
        if (tarjeta.getFechaFinalizacion() != null) {
            builder.put("fechaFinalizacion", tarjeta.getFechaFinalizacion().format(TIME_FORMATTER));
        }
        
        if (tarjeta.getFechaEntrega() != null) {
            builder.put("fechaEntrega", tarjeta.getFechaEntrega().format(TIME_FORMATTER));
        }
        
        builder.put("createdAt", tarjeta.getCreatedAt().format(TIME_FORMATTER));
        builder.put("updatedAt", tarjeta.getUpdatedAt().format(TIME_FORMATTER));
        
        return builder;
    }
}