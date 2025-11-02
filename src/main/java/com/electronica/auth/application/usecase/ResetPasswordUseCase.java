package com.electronica.auth.application.usecase;

import com.electronica.auth.domain.model.User;
import com.electronica.auth.domain.port.EmailServicePort;
import com.electronica.auth.domain.port.UserRepositoryPort;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.UUID;

public class ResetPasswordUseCase {
    private final UserRepositoryPort userRepository;
    private final EmailServicePort emailService;

    public ResetPasswordUseCase(UserRepositoryPort userRepository, EmailServicePort emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public void requestReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Si el correo existe en el sistema, le llegará un enlace"));

        // Generar token de recuperación
        String resetToken = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusHours(1);

        // Guardar token en BD
        userRepository.updateResetToken(user.getId(), resetToken, expiry);

        // Enviar email
        String resetLink = "http://localhost:7000/reset-password?token=" + resetToken;
        emailService.sendPasswordResetEmail(user.getCorreoElectronico(), resetLink);
    }

    public void updatePassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido o expirado"));

        // Verificar que el token no haya expirado
        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expirado");
        }

        // Encriptar nueva contraseña
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        // Actualizar contraseña
        userRepository.updatePassword(user.getId(), hashedPassword);

        // Limpiar token
        userRepository.updateResetToken(user.getId(), null, null);
    }
}