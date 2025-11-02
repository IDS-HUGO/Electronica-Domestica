package com.electronica.auth.infrastructure.adapter;

import com.electronica.auth.domain.model.User;
import com.electronica.auth.domain.port.UserRepositoryPort;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class UserRepositoryAdapter implements UserRepositoryPort {
    private final DataSource dataSource;

    public UserRepositoryAdapter(DataSource dataSource) {
        this.dataSource = dataSource;
        initializeDatabase();
    }

    private void initializeDatabase() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS users (
                id VARCHAR(36) PRIMARY KEY,
                nombre_completo VARCHAR(255) NOT NULL,
                correo_electronico VARCHAR(255) UNIQUE NOT NULL,
                contrasena VARCHAR(255) NOT NULL,
                tipo VARCHAR(50) NOT NULL,
                reset_token VARCHAR(36),
                reset_token_expiry TIMESTAMP,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Error al inicializar la base de datos", e);
        }
    }

    @Override
    public User save(User user) {
        String sql = """
            INSERT INTO users (id, nombre_completo, correo_electronico, contrasena, tipo, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getId());
            stmt.setString(2, user.getNombreCompleto());
            stmt.setString(3, user.getCorreoElectronico());
            stmt.setString(4, user.getContrasena());
            stmt.setString(5, user.getTipo());
            stmt.setTimestamp(6, Timestamp.valueOf(user.getCreatedAt()));
            stmt.setTimestamp(7, Timestamp.valueOf(user.getUpdatedAt()));

            stmt.executeUpdate();
            return user;

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar usuario", e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE correo_electronico = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por email", e);
        }
    }

    @Override
    public Optional<User> findById(String id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por id", e);
        }
    }

    @Override
    public Optional<User> findByResetToken(String token) {
        String sql = "SELECT * FROM users WHERE reset_token = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por reset token", e);
        }
    }

    @Override
    public void updatePassword(String userId, String newPassword) {
        String sql = "UPDATE users SET contrasena = ?, updated_at = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPassword);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(3, userId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar contraseña", e);
        }
    }

    @Override
    public void updateResetToken(String userId, String token, LocalDateTime expiry) {
        String sql = "UPDATE users SET reset_token = ?, reset_token_expiry = ?, updated_at = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token);
            stmt.setTimestamp(2, expiry != null ? Timestamp.valueOf(expiry) : null);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(4, userId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar reset token", e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE correo_electronico = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;

        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de email", e);
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setNombreCompleto(rs.getString("nombre_completo"));
        user.setCorreoElectronico(rs.getString("correo_electronico"));
        user.setContrasena(rs.getString("contrasena"));
        user.setTipo(rs.getString("tipo"));
        user.setResetToken(rs.getString("reset_token"));

        Timestamp resetTokenExpiry = rs.getTimestamp("reset_token_expiry");
        if (resetTokenExpiry != null) {
            user.setResetTokenExpiry(resetTokenExpiry.toLocalDateTime());
        }

        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

        return user;
    }
}