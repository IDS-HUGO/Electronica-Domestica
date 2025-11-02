package com.electronica.auth.domain.port;

import com.electronica.auth.domain.model.User;
import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(String id);
    Optional<User> findByResetToken(String token);
    void updatePassword(String userId, String newPassword);
    void updateResetToken(String userId, String token, java.time.LocalDateTime expiry);
    boolean existsByEmail(String email);
}