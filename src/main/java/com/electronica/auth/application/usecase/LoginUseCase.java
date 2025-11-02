package com.electronica.auth.application.usecase;

import com.electronica.auth.application.dto.AuthResponseDto;
import com.electronica.auth.application.dto.LoginRequestDto;
import com.electronica.auth.domain.model.User;
import com.electronica.auth.domain.port.UserRepositoryPort;
import org.mindrot.jbcrypt.BCrypt;

public class LoginUseCase {
    private final UserRepositoryPort userRepository;
    private final JwtService jwtService;

    public LoginUseCase(UserRepositoryPort userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public AuthResponseDto execute(LoginRequestDto request) {
        // Buscar usuario por email
        User user = userRepository.findByEmail(request.getCorreoElectronico())
                .orElseThrow(() -> new IllegalArgumentException("Usuario o contraseña incorrecta"));

        // Verificar contraseña
        if (!BCrypt.checkpw(request.getContrasena(), user.getContrasena())) {
            throw new IllegalArgumentException("Usuario o contraseña incorrecta");
        }

        // Generar token JWT
        String token = jwtService.generateToken(user.getId(), user.getCorreoElectronico());

        // Retornar respuesta
        return new AuthResponseDto(
                token,
                user.getId(),
                user.getCorreoElectronico(),
                user.getNombreCompleto(),
                user.getTipo()
        );
    }
}