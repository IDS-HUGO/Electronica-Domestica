package com.electronica.auth.application.usecase;

import com.electronica.auth.application.dto.AuthResponseDto;
import com.electronica.auth.application.dto.RegisterRequestDto;
import com.electronica.auth.domain.model.User;
import com.electronica.auth.domain.port.UserRepositoryPort;
import org.mindrot.jbcrypt.BCrypt;

public class RegisterUseCase {
    private final UserRepositoryPort userRepository;
    private final JwtService jwtService;

    public RegisterUseCase(UserRepositoryPort userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public AuthResponseDto execute(RegisterRequestDto request) {
        // Validar que el email no exista
        if (userRepository.existsByEmail(request.getCorreoElectronico())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }

        // Crear usuario
        User user = new User();
        user.setNombreCompleto(request.getNombreCompleto());
        user.setCorreoElectronico(request.getCorreoElectronico());
        user.setTipo(request.getTipo());

        // Encriptar contraseña
        String hashedPassword = BCrypt.hashpw(request.getContrasena(), BCrypt.gensalt());
        user.setContrasena(hashedPassword);

        // Guardar usuario
        User savedUser = userRepository.save(user);

        // Generar token JWT
        String token = jwtService.generateToken(savedUser.getId(), savedUser.getCorreoElectronico());

        // Retornar respuesta
        return new AuthResponseDto(
                token,
                savedUser.getId(),
                savedUser.getCorreoElectronico(),
                savedUser.getNombreCompleto(),
                savedUser.getTipo()
        );
    }
}