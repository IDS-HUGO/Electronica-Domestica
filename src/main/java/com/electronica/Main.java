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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Main {

    private static final String VERSION = "1.0.0";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static void main(String[] args) {
        try {
            EnvConfig.validateConfig();

            DataSource dataSource = DatabaseConfig.createDataSource();
            System.out.println("✅ Conexión a BD establecida");

            // Servicios
            UserRepository userRepo = new UserRepository(dataSource);
            EmailService emailService = new EmailService();
            AuthService authService = new AuthService(userRepo, emailService);

            TarjetaRepository tarjetaRepo = new TarjetaRepository(dataSource);
            TarjetaService tarjetaService = new TarjetaService(tarjetaRepo);

            ProductoRepository productoRepo = new ProductoRepository(dataSource);
            ProductoService productoService = new ProductoService(productoRepo);

            FinalizadoRepository finalizadoRepo = new FinalizadoRepository(dataSource);
            FinalizadoService finalizadoService = new FinalizadoService(finalizadoRepo);

            // Servidor
            Javalin app = Javalin.create(config -> {
                config.bundledPlugins.enableCors(cors -> cors.addRule(it -> it.anyHost()));
                config.jsonMapper(new JavalinJackson());
            });

            // Rutas
            app.get("/api/health", ctx -> ctx.json(Map.of(
                    "status", "OK",
                    "version", VERSION,
                    "timestamp", LocalDateTime.now().format(FORMATTER)
            )));

            AuthRoutes.register(app, authService);
            TarjetaRoutes.register(app, tarjetaService);
            ProductoRoutes.register(app, productoService);
            FinalizadoRoutes.register(app, finalizadoService);

            // 404 y 500
            app.error(404, ctx -> ctx.json(Map.of("success", false, "message", "Endpoint no encontrado")));
            app.exception(Exception.class, (e, ctx) -> {
                ctx.status(500).json(Map.of("success", false, "message", "Error interno del servidor"));
            });

            int port = EnvConfig.getServerPort();
            app.start(port);

            System.out.println("✅ API iniciada en http://localhost:" + port);

        } catch (Exception e) {
            System.err.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}