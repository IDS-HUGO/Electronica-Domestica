// UBICACIÓN: src/main/java/com/electronica/Main.java

package com.electronica;

import com.electronica.auth.application.usecase.JwtService;
import com.electronica.auth.application.usecase.LoginUseCase;
import com.electronica.auth.application.usecase.RegisterUseCase;
import com.electronica.auth.application.usecase.ResetPasswordUseCase;
import com.electronica.auth.domain.port.EmailServicePort;
import com.electronica.auth.domain.port.UserRepositoryPort;
import com.electronica.auth.infrastructure.adapter.EmailServiceAdapter;
import com.electronica.auth.infrastructure.adapter.UserRepositoryAdapter;
import com.electronica.auth.infrastructure.config.CorsConfig;
import com.electronica.auth.infrastructure.config.DatabaseConfig;
import com.electronica.auth.infrastructure.controller.AuthController;
import io.javalin.Javalin;

import javax.sql.DataSource;

public class Main {

    public static void main(String[] args) {
        // Configuración
        int port = 7000;
        String jwtSecret = "mi-super-secreto-jwt-key-2024"; // En producción usar variable de entorno
        long jwtExpiration = 86400000; // 24 horas en milisegundos

        // Email config (cambiar por tus credenciales reales)
        String emailFrom = "tu-email@gmail.com";
        String emailPassword = "tu-password-app";
        String smtpHost = "smtp.gmail.com";
        String smtpPort = "587";

        // Inicializar componentes
        DataSource dataSource = DatabaseConfig.createDataSource();

        // Puertos (Adapters)
        UserRepositoryPort userRepository = new UserRepositoryAdapter(dataSource);
        EmailServicePort emailService = new EmailServiceAdapter(emailFrom, emailPassword, smtpHost, smtpPort);

        // Servicios
        JwtService jwtService = new JwtService(jwtSecret, jwtExpiration);

        // Casos de uso
        RegisterUseCase registerUseCase = new RegisterUseCase(userRepository, jwtService);
        LoginUseCase loginUseCase = new LoginUseCase(userRepository, jwtService);
        ResetPasswordUseCase resetPasswordUseCase = new ResetPasswordUseCase(userRepository, emailService);

        // Controlador
        AuthController authController = new AuthController(registerUseCase, loginUseCase, resetPasswordUseCase);

        // Crear aplicación Javalin
        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> {
                cors.add(it -> {
                    it.anyHost();
                });
            });
        });

        // Configurar CORS
        CorsConfig.configure(app);

        // Rutas
        app.post("/api/auth/register", authController::register);
        app.post("/api/auth/login", authController::login);
        app.post("/api/auth/request-reset", authController::requestPasswordReset);
        app.post("/api/auth/reset-password", authController::resetPassword);

        // Ruta de health check
        app.get("/api/health", ctx -> ctx.json(java.util.Map.of(
                "status", "OK",
                "message", "API funcionando correctamente"
        )));

        // Iniciar servidor
        app.start(port);

        System.out.println("=============================================================");
        System.out.println("");
        System.out.println("    API Electronica Domestica - INICIADA");
        System.out.println("");
        System.out.println("    Puerto: " + port);
        System.out.println("    URL: http://localhost:" + port);
        System.out.println("");
        System.out.println("    Endpoints disponibles:");
        System.out.println("    POST /api/auth/register");
        System.out.println("    POST /api/auth/login");
        System.out.println("    POST /api/auth/request-reset");
        System.out.println("    POST /api/auth/reset-password");
        System.out.println("    GET  /api/health");
        System.out.println("");
        System.out.println("=============================================================");
    }
}