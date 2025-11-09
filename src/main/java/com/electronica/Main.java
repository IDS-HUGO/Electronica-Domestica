package com.electronica;

import com.electronica.config.DatabaseConfig;
import com.electronica.config.EnvConfig;
import com.electronica.auth.routes.AuthRoutes;
import com.electronica.auth.repositories.UserRepository;
import com.electronica.auth.services.AuthService;
import com.electronica.auth.services.EmailService;

import com.electronica.tarjeta.routes.TarjetaRoutes;
import com.electronica.tarjeta.repositories.TarjetaRepository;
import com.electronica.tarjeta.services.TarjetaService;

import com.electronica.producto.routes.ProductoRoutes;
import com.electronica.producto.repositories.ProductoRepository;
import com.electronica.producto.services.ProductoService;

import com.electronica.finalizado.routes.FinalizadoRoutes;
import com.electronica.finalizado.repositories.FinalizadoRepository;
import com.electronica.finalizado.services.FinalizadoService;

import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

import javax.sql.DataSource;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("\n" + "=".repeat(65));
            System.out.println("🚀 INICIANDO API - " + EnvConfig.getAppName());
            System.out.println("=".repeat(65) + "\n");

            EnvConfig.validateConfig();

            DataSource dataSource = DatabaseConfig.createDataSource();
            System.out.println("✅ Conexión a base de datos establecida\n");

            // =============================================================
            // INICIALIZAR SERVICIOS
            // =============================================================
            UserRepository userRepository = new UserRepository(dataSource);
            EmailService emailService = new EmailService();
            AuthService authService = new AuthService(userRepository, emailService);

            TarjetaRepository tarjetaRepository = new TarjetaRepository(dataSource);
            TarjetaService tarjetaService = new TarjetaService(tarjetaRepository);

            ProductoRepository productoRepository = new ProductoRepository(dataSource);
            ProductoService productoService = new ProductoService(productoRepository);

            FinalizadoRepository finalizadoRepository = new FinalizadoRepository(dataSource);
            FinalizadoService finalizadoService = new FinalizadoService(finalizadoRepository);

            // =============================================================
            // CREAR JAVALIN 6
            // =============================================================
            Javalin app = Javalin.create(config -> {
                config.bundledPlugins.enableCors(cors -> {
                    cors.addRule(it -> it.anyHost());
                });
                config.jsonMapper(new JavalinJackson());
            });

            // =============================================================
            // REGISTRAR RUTAS DE CADA MÓDULO
            // =============================================================
            AuthRoutes.register(app, authService);
            TarjetaRoutes.register(app, tarjetaService);
            ProductoRoutes.register(app, productoService);
            FinalizadoRoutes.register(app, finalizadoService);

            // Health Check
            app.get("/api/health", ctx -> ctx.json(java.util.Map.of(
                    "status", "OK",
                    "javaVersion", System.getProperty("java.version"),
                    "javalinVersion", "6.3.0"
            )));

            // =============================================================
            // INICIAR SERVIDOR
            // =============================================================
            int port = EnvConfig.getServerPort();
            app.start(port);

            System.out.println("\n✅ API INICIADA EN http://localhost:" + port + "\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}