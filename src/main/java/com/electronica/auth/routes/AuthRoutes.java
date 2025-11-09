package com.electronica.auth.routes;

import com.electronica.auth.services.AuthService;
import io.javalin.Javalin;

public class AuthRoutes {

    public static void register(Javalin app, AuthService authService) {
        app.post("/api/auth/register", authService::register);
        app.post("/api/auth/login", authService::login);
        app.post("/api/auth/request-reset", authService::requestPasswordReset);
        app.post("/api/auth/reset-password", authService::resetPassword);
    }
}