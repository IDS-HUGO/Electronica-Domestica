package com.electronica.producto.services;

import com.electronica.producto.models.Producto;
import com.electronica.producto.repositories.ProductoRepository;
import io.javalin.http.Context;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductoService {
    private final ProductoRepository repository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MMMM/yyyy");

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public void create(Context ctx) {
        try {
            var body = ctx.bodyAsClass(Map.class);

            Producto producto = new Producto();
            producto.setNombreProducto((String) body.get("nombreProducto"));
            producto.setCategoria((String) body.get("categoria"));

            if (body.get("cantidadOhms") != null) {
                producto.setCantidadOhms(new BigDecimal(body.get("cantidadOhms").toString()));
            }

            producto.setUnidad((String) body.get("unidad"));
            producto.setCantidadPiezas((Integer) body.get("cantidadPiezas"));

            if (body.get("precioUnitario") != null) {
                producto.setPrecioUnitario(new BigDecimal(body.get("precioUnitario").toString()));
            }

            Producto saved = repository.save(producto);

            ctx.status(201).json(Map.of(
                    "success", true,
                    "message", "Producto creado exitosamente",
                    "data", mapToResponse(saved)
            ));

        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error al crear producto"
            ));
        }
    }

    public void getAll(Context ctx) {
        try {
            List<Producto> productos = repository.findAll();

            List<Map<String, Object>> response = productos.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            ctx.json(Map.of(
                    "success", true,
                    "message", "Productos obtenidos exitosamente",
                    "data", response
            ));

        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error al obtener productos"
            ));
        }
    }

    public void getById(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            Producto producto = repository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

            ctx.json(Map.of(
                    "success", true,
                    "message", "Producto obtenido exitosamente",
                    "data", mapToResponse(producto)
            ));

        } catch (IllegalArgumentException e) {
            ctx.status(404).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error al obtener producto"
            ));
        }
    }

    public void update(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            var body = ctx.bodyAsClass(Map.class);

            Producto existing = repository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

            existing.setNombreProducto((String) body.get("nombreProducto"));
            existing.setCategoria((String) body.get("categoria"));

            if (body.get("cantidadOhms") != null) {
                existing.setCantidadOhms(new BigDecimal(body.get("cantidadOhms").toString()));
            }

            existing.setUnidad((String) body.get("unidad"));
            existing.setCantidadPiezas((Integer) body.get("cantidadPiezas"));

            if (body.get("precioUnitario") != null) {
                existing.setPrecioUnitario(new BigDecimal(body.get("precioUnitario").toString()));
            }

            Producto updated = repository.update(id, existing);

            ctx.json(Map.of(
                    "success", true,
                    "message", "Producto actualizado exitosamente",
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
                    "message", "Error al actualizar producto"
            ));
        }
    }

    public void delete(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            repository.deleteById(id);

            ctx.json(Map.of(
                    "success", true,
                    "message", "Producto eliminado exitosamente"
            ));

        } catch (IllegalArgumentException e) {
            ctx.status(404).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error al eliminar producto"
            ));
        }
    }

    private Map<String, Object> mapToResponse(Producto producto) {
        return Map.of(
                "id", producto.getId(),
                "nombreProducto", producto.getNombreProducto(),
                "categoria", producto.getCategoria(),
                "cantidadOhms", producto.getCantidadOhms() != null ? producto.getCantidadOhms() : 0,
                "unidad", producto.getUnidad() != null ? producto.getUnidad() : "",
                "cantidadPiezas", producto.getCantidadPiezas(),
                "precioUnitario", producto.getPrecioUnitario(),
                "fechaRegistro", producto.getFechaRegistro().format(FORMATTER)
        );
    }
}