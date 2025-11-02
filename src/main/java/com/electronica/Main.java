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
import com.electronica.auth.infrastructure.config.EnvConfig;
import com.electronica.auth.infrastructure.controller.AuthController;
import io.javalin.Javalin;

import javax.sql.DataSource;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("\n" + "=".repeat(65));
            System.out.println("🚀 INICIANDO API - " + EnvConfig.getAppName());
            System.out.println("=".repeat(65) + "\n");

            // Validar configuración
            EnvConfig.validateConfig();

            // Inicializar componentes
            DataSource dataSource = DatabaseConfig.createDataSource();
            System.out.println("✅ Conexión a base de datos establecida\n");

            // Puertos (Adapters)
            UserRepositoryPort userRepository = new UserRepositoryAdapter(dataSource);
            EmailServicePort emailService = new EmailServiceAdapter();

            // Servicios
            JwtService jwtService = new JwtService(
                    EnvConfig.getJwtSecret(),
                    EnvConfig.getJwtExpiration()
            );

            // Casos de uso
            RegisterUseCase registerUseCase = new RegisterUseCase(userRepository, jwtService);
            LoginUseCase loginUseCase = new LoginUseCase(userRepository, jwtService);
            ResetPasswordUseCase resetPasswordUseCase = new ResetPasswordUseCase(
                    userRepository,
                    emailService
            );

            // Controlador
            AuthController authController = new AuthController(
                    registerUseCase,
                    loginUseCase,
                    resetPasswordUseCase
            );

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
                    "message", "API funcionando correctamente",
                    "database", "MySQL - electronica_domestica",
                    "email", "Gmail - " + EnvConfig.getEmailFrom()
            )));

            // Iniciar servidor
            int port = EnvConfig.getServerPort();
            app.start(port);

            System.out.println("\n" + "=".repeat(65));
            System.out.println("");
            System.out.println("    ✅ API INICIADA EXITOSAMENTE");
            System.out.println("");
            System.out.println("    🌐 Puerto: " + port);
            System.out.println("    🔗 URL: http://localhost:" + port);
            System.out.println("    📧 Email: " + EnvConfig.getEmailFrom());
            System.out.println("");
            System.out.println("    📚 Endpoints disponibles:");
            System.out.println("       POST /api/auth/register");
            System.out.println("       POST /api/auth/login");
            System.out.println("       POST /api/auth/request-reset");
            System.out.println("       POST /api/auth/reset-password");
            System.out.println("       GET  /api/health");
            System.out.println("");
            System.out.println("=".repeat(65) + "\n");

        } catch (Exception e) {
            System.err.println("\n" + "=".repeat(65));
            System.err.println("❌ ERROR AL INICIAR LA API");
            System.err.println("=".repeat(65));
            System.err.println("\n" + e.getMessage() + "\n");
            e.printStackTrace();
            System.exit(1);
        }
    }
}