package com.electronica.finalizado.routes;

import com.electronica.config.JwtConfig;
import com.electronica.finalizado.services.FinalizadoService;
import io.javalin.Javalin;

public class FinalizadoRoutes {
    public static void register(Javalin app, FinalizadoService finalizadoService) {
        app.before("/api/finalizado*", JwtConfig::validateToken);

        app.post("/api/finalizado", finalizadoService::create);
        app.get("/api/finalizado", finalizadoService::getAll);
        app.get("/api/finalizado/{id}", finalizadoService::getById);
        app.get("/api/finalizado/tecnico/{tecnicoId}", finalizadoService::getByTecnico);
        app.put("/api/finalizado/{id}", finalizadoService::update);
        app.delete("/api/finalizado/{id}", finalizadoService::delete);
    }
}