package com.electronica.tarjeta.routes;

import com.electronica.config.JwtConfig;
import com.electronica.tarjeta.services.TarjetaService;
import io.javalin.Javalin;

public class TarjetaRoutes {

    public static void register(Javalin app, TarjetaService tarjetaService) {
        app.before("/api/tarjetas", JwtConfig::validateToken);

        app.post("/api/tarjetas", tarjetaService::create);
        app.get("/api/tarjetas", tarjetaService::getAll);
        app.get("/api/tarjetas/{id}", tarjetaService::getById);
        app.put("/api/tarjetas/{id}", tarjetaService::update);
        app.delete("/api/tarjetas/{id}", tarjetaService::delete);
    }
}