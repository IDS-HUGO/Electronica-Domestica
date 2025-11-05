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

// Imports de Registro de Tarjetas
import com.electronica.tarjeta.application.usecase.*;
import com.electronica.tarjeta.domain.port.RegistroTarjetaRepositoryPort;
import com.electronica.tarjeta.infrastructure.adapter.RegistroTarjetaRepositoryAdapter;
import com.electronica.tarjeta.infrastructure.controller.RegistroTarjetaController;

// Imports de Productos (NOTA: Debes completar estos Use Cases y Controller)
import com.electronica.producto.domain.port.ProductoRepositoryPort;
import com.electronica.producto.infrastructure.adapter.ProductoRepositoryAdapter;
// import com.electronica.producto.application.usecase.*;
// import com.electronica.producto.infrastructure.controller.ProductoController;

// Imports de Registro Finalizado (NOTA: Debes completar estos Use Cases y Controller)
import com.electronica.finalizado.domain.port.RegistroFinalizadoRepositoryPort;
import com.electronica.finalizado.infrastructure.adapter.RegistroFinalizadoRepositoryAdapter;
// import com.electronica.finalizado.application.usecase.*;
// import com.electronica.finalizado.infrastructure.controller.RegistroFinalizadoController;

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

            // =============================================================
            // MÓDULO: AUTENTICACIÓN
            // =============================================================
            UserRepositoryPort userRepository = new UserRepositoryAdapter(dataSource);
            EmailServicePort emailService = new EmailServiceAdapter();

            JwtService jwtService = new JwtService(
                    EnvConfig.getJwtSecret(),
                    EnvConfig.getJwtExpiration()
            );

            RegisterUseCase registerUseCase = new RegisterUseCase(userRepository, jwtService);
            LoginUseCase loginUseCase = new LoginUseCase(userRepository, jwtService);
            ResetPasswordUseCase resetPasswordUseCase = new ResetPasswordUseCase(
                    userRepository,
                    emailService
            );

            AuthController authController = new AuthController(
                    registerUseCase,
                    loginUseCase,
                    resetPasswordUseCase
            );

            // =============================================================
            // MÓDULO: REGISTRO DE TARJETAS
            // =============================================================
            RegistroTarjetaRepositoryPort tarjetaRepository =
                    new RegistroTarjetaRepositoryAdapter(dataSource);

            CreateRegistroTarjetaUseCase createTarjetaUseCase =
                    new CreateRegistroTarjetaUseCase(tarjetaRepository);
            GetAllRegistroTarjetasUseCase getAllTarjetasUseCase =
                    new GetAllRegistroTarjetasUseCase(tarjetaRepository);
            GetRegistroTarjetaByIdUseCase getTarjetaByIdUseCase =
                    new GetRegistroTarjetaByIdUseCase(tarjetaRepository);
            UpdateRegistroTarjetaUseCase updateTarjetaUseCase =
                    new UpdateRegistroTarjetaUseCase(tarjetaRepository);
            DeleteRegistroTarjetaUseCase deleteTarjetaUseCase =
                    new DeleteRegistroTarjetaUseCase(tarjetaRepository);

            RegistroTarjetaController tarjetaController = new RegistroTarjetaController(
                    createTarjetaUseCase,
                    getAllTarjetasUseCase,
                    getTarjetaByIdUseCase,
                    updateTarjetaUseCase,
                    deleteTarjetaUseCase
            );

            // =============================================================
            // MÓDULO: PRODUCTOS
            // =============================================================
            ProductoRepositoryPort productoRepository =
                    new ProductoRepositoryAdapter(dataSource);

            // NOTA: Aquí deberás instanciar los Use Cases de Productos
            // CreateProductoUseCase createProductoUseCase = new CreateProductoUseCase(productoRepository);
            // ... etc

            // ProductoController productoController = new ProductoController(...);

            // =============================================================
            // MÓDULO: REGISTRO FINALIZADO
            // =============================================================
            RegistroFinalizadoRepositoryPort finalizadoRepository =
                    new RegistroFinalizadoRepositoryAdapter(dataSource);

            // NOTA: Aquí deberás instanciar los Use Cases de Registro Finalizado
            // CreateRegistroFinalizadoUseCase createFinalizadoUseCase = ...
            // ... etc

            // RegistroFinalizadoController finalizadoController = new RegistroFinalizadoController(...);

            // =============================================================
            // CREAR APLICACIÓN JAVALIN
            // =============================================================
            Javalin app = Javalin.create(config -> {
                config.plugins.enableCors(cors -> {
                    cors.add(it -> {
                        it.anyHost();
                    });
                });
            });

            // Configurar CORS
            CorsConfig.configure(app);

            // =============================================================
            // RUTAS: AUTENTICACIÓN
            // =============================================================
            app.post("/api/auth/register", authController::register);
            app.post("/api/auth/login", authController::login);
            app.post("/api/auth/request-reset", authController::requestPasswordReset);
            app.post("/api/auth/reset-password", authController::resetPassword);

            // =============================================================
            // RUTAS: REGISTRO DE TARJETAS (CRUD COMPLETO)
            // CAMBIO IMPORTANTE: :id → {id}
            // =============================================================
            app.post("/api/tarjetas", tarjetaController::create);           // CREATE
            app.get("/api/tarjetas", tarjetaController::getAll);            // READ ALL
            app.get("/api/tarjetas/{id}", tarjetaController::getById);      // READ ONE ← CAMBIO AQUÍ
            app.put("/api/tarjetas/{id}", tarjetaController::update);       // UPDATE ← CAMBIO AQUÍ
            app.delete("/api/tarjetas/{id}", tarjetaController::delete);    // DELETE ← CAMBIO AQUÍ

            // =============================================================
            // RUTAS: PRODUCTOS (CRUD COMPLETO) - DESCOMENTAR CUANDO ESTÉ LISTO
            // =============================================================
            /*
            app.post("/api/productos", productoController::create);
            app.get("/api/productos", productoController::getAll);
            app.get("/api/productos/{id}", productoController::getById);
            app.put("/api/productos/{id}", productoController::update);
            app.delete("/api/productos/{id}", productoController::delete);
            app.get("/api/productos/categoria/{categoria}", productoController::getByCategoria);
            app.get("/api/productos/stock-bajo", productoController::getLowStock);
            */

            // =============================================================
            // RUTAS: REGISTRO FINALIZADO (CRUD COMPLETO) - DESCOMENTAR CUANDO ESTÉ LISTO
            // =============================================================
            /*
            app.post("/api/finalizados", finalizadoController::create);
            app.get("/api/finalizados", finalizadoController::getAll);
            app.get("/api/finalizados/{id}", finalizadoController::getById);
            app.put("/api/finalizados/{id}", finalizadoController::update);
            app.delete("/api/finalizados/{id}", finalizadoController::delete);
            app.get("/api/finalizados/tecnico/{tecnicoId}", finalizadoController::getByTecnico);
            */

            // =============================================================
            // RUTA DE HEALTH CHECK
            // =============================================================
            app.get("/api/health", ctx -> ctx.json(java.util.Map.of(
                    "status", "OK",
                    "message", "API funcionando correctamente",
                    "database", "MySQL - electronica_domestica",
                    "email", "Gmail - " + EnvConfig.getEmailFrom(),
                    "modules", java.util.List.of(
                            "Autenticación",
                            "Registro de Tarjetas",
                            "Productos (pendiente)",
                            "Registro Finalizado (pendiente)"
                    )
            )));

            // =============================================================
            // INICIAR SERVIDOR
            // =============================================================
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
            System.out.println("");
            System.out.println("    🔐 AUTENTICACIÓN:");
            System.out.println("       POST   /api/auth/register");
            System.out.println("       POST   /api/auth/login");
            System.out.println("       POST   /api/auth/request-reset");
            System.out.println("       POST   /api/auth/reset-password");
            System.out.println("");
            System.out.println("    🔧 REGISTRO DE TARJETAS:");
            System.out.println("       POST   /api/tarjetas");
            System.out.println("       GET    /api/tarjetas");
            System.out.println("       GET    /api/tarjetas/{id}");
            System.out.println("       PUT    /api/tarjetas/{id}");
            System.out.println("       DELETE /api/tarjetas/{id}");
            System.out.println("");
            System.out.println("    📦 PRODUCTOS: (pendiente implementación completa)");
            System.out.println("       POST   /api/productos");
            System.out.println("       GET    /api/productos");
            System.out.println("       GET    /api/productos/{id}");
            System.out.println("       PUT    /api/productos/{id}");
            System.out.println("       DELETE /api/productos/{id}");
            System.out.println("");
            System.out.println("    ✅ REGISTRO FINALIZADO: (pendiente implementación completa)");
            System.out.println("       POST   /api/finalizados");
            System.out.println("       GET    /api/finalizados");
            System.out.println("       GET    /api/finalizados/{id}");
            System.out.println("       PUT    /api/finalizados/{id}");
            System.out.println("       DELETE /api/finalizados/{id}");
            System.out.println("");
            System.out.println("    🏥 SALUD:");
            System.out.println("       GET    /api/health");
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