package com.electronica.producto.routes;

import com.electronica.config.JwtConfig;
import com.electronica.producto.services.ProductoService;
import io.javalin.Javalin;

public class ProductoRoutes {

    public static void register(Javalin app, ProductoService productoService) {
        // Aplicar JWT a todas las rutas de productos
        app.before("/api/productos*", JwtConfig::validateToken);

        app.post("/api/productos", productoService::create);
        app.get("/api/productos", productoService::getAll);
        app.get("/api/productos/{id}", productoService::getById);
        app.put("/api/productos/{id}", productoService::update);
        app.delete("/api/productos/{id}", productoService::delete);
    }
}