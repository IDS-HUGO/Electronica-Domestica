package com.electronica.auth.infrastructure.controller;

import com.electronica.auth.application.dto.AuthResponseDto;
import com.electronica.auth.application.dto.LoginRequestDto;
import com.electronica.auth.application.dto.RegisterRequestDto;
import com.electronica.auth.application.usecase.LoginUseCase;
import com.electronica.auth.application.usecase.RegisterUseCase;
import com.electronica.auth.application.usecase.ResetPasswordUseCase;
import io.javalin.http.Context;

import java.util.Map;

public class AuthController {
    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;

    public AuthController(RegisterUseCase registerUseCase, LoginUseCase loginUseCase, ResetPasswordUseCase resetPasswordUseCase) {
        this.registerUseCase = registerUseCase;
        this.loginUseCase = loginUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
    }

    public void register(Context ctx) {
        try {
            RegisterRequestDto request = ctx.bodyAsClass(RegisterRequestDto.class);
            AuthResponseDto response = registerUseCase.execute(request);

            ctx.status(201).json(Map.of(
                    "success", true,
                    "message", "Usuario registrado exitosamente",
                    "data", response
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

    public void login(Context ctx) {
        try {
            LoginRequestDto request = ctx.bodyAsClass(LoginRequestDto.class);
            AuthResponseDto response = loginUseCase.execute(request);

            ctx.json(Map.of(
                    "success", true,
                    "message", "Inicio de sesión exitoso",
                    "data", response
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
            Map<String, String> body = ctx.bodyAsClass(Map.class);
            String email = body.get("correoElectronico");

            resetPasswordUseCase.requestReset(email);

            ctx.json(Map.of(
                    "success", true,
                    "message", "Si el correo existe en el sistema, le llegará un enlace"
            ));

        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error interno del servidor"
            ));
        }
    }

    public void resetPassword(Context ctx) {
        try {
            Map<String, String> body = ctx.bodyAsClass(Map.class);
            String token = body.get("token");
            String newPassword = body.get("nuevaContrasena");

            resetPasswordUseCase.updatePassword(token, newPassword);

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