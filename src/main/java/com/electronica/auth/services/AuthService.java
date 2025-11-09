package com.electronica.auth.services;

import com.electronica.config.EnvConfig;
import com.electronica.config.JwtConfig;
import com.electronica.auth.models.User;
import com.electronica.auth.repositories.UserRepository;
import io.javalin.http.Context;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class AuthService {
    private final UserRepository userRepository;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public void register(Context ctx) {
        try {
            var body = ctx.bodyAsClass(Map.class);

            String email = (String) body.get("correoElectronico");

            if (userRepository.existsByEmail(email)) {
                ctx.status(400).json(Map.of(
                        "success", false,
                        "message", "El correo ya está registrado"
                ));
                return;
            }

            User user = new User();
            user.setNombreCompleto((String) body.get("nombreCompleto"));
            user.setCorreoElectronico(email);
            user.setTipo((String) body.get("tipo"));
            user.setContrasena(BCrypt.hashpw((String) body.get("contrasena"), BCrypt.gensalt()));

            User saved = userRepository.save(user);

            String token = JwtConfig.generateToken(saved.getId(), saved.getCorreoElectronico());

            ctx.status(201).json(Map.of(
                    "success", true,
                    "message", "Usuario registrado exitosamente",
                    "data", Map.of(
                            "token", token,
                            "userId", saved.getId(),
                            "email", saved.getCorreoElectronico(),
                            "nombreCompleto", saved.getNombreCompleto(),
                            "tipo", saved.getTipo()
                    )
            ));

        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error interno del servidor"
            ));
        }
    }

    public void login(Context ctx) {
        try {
            var body = ctx.bodyAsClass(Map.class);

            String email = (String) body.get("correoElectronico");
            String password = (String) body.get("contrasena");

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

            if (!BCrypt.checkpw(password, user.getContrasena())) {
                throw new IllegalArgumentException("Credenciales inválidas");
            }

            String token = JwtConfig.generateToken(user.getId(), user.getCorreoElectronico());

            ctx.json(Map.of(
                    "success", true,
                    "message", "Inicio de sesión exitoso",
                    "data", Map.of(
                            "token", token,
                            "userId", user.getId(),
                            "email", user.getCorreoElectronico(),
                            "nombreCompleto", user.getNombreCompleto(),
                            "tipo", user.getTipo()
                    )
            ));

        } catch (IllegalArgumentException e) {
            ctx.status(401).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error interno del servidor"
            ));
        }
    }

    public void requestPasswordReset(Context ctx) {
        try {
            var body = ctx.bodyAsClass(Map.class);
            String email = (String) body.get("correoElectronico");

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            String resetToken = UUID.randomUUID().toString();
            LocalDateTime expiry = LocalDateTime.now().plusHours(1);

            userRepository.updateResetToken(user.getId(), resetToken, expiry);

            String resetLink = EnvConfig.getAppFrontendUrl() + "/reset-password?token=" + resetToken;
            emailService.sendPasswordResetEmail(user.getCorreoElectronico(), resetLink);

            ctx.json(Map.of(
                    "success", true,
                    "message", "Email de recuperación enviado"
            ));

        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error al enviar email"
            ));
        }
    }

    public void resetPassword(Context ctx) {
        try {
            var body = ctx.bodyAsClass(Map.class);
            String token = (String) body.get("token");
            String newPassword = (String) body.get("nuevaContrasena");

            User user = userRepository.findByResetToken(token)
                    .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

            if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Token expirado");
            }

            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            userRepository.updatePassword(user.getId(), hashedPassword);
            userRepository.updateResetToken(user.getId(), null, null);

            ctx.json(Map.of(
                    "success", true,
                    "message", "Contraseña actualizada exitosamente"
            ));

        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error interno del servidor"
            ));
        }
    }
}