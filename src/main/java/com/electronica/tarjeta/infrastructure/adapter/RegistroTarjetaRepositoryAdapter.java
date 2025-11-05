package com.electronica.tarjeta.infrastructure.adapter;

import com.electronica.tarjeta.domain.model.RegistroTarjeta;
import com.electronica.tarjeta.domain.port.RegistroTarjetaRepositoryPort;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RegistroTarjetaRepositoryAdapter implements RegistroTarjetaRepositoryPort {
    private final DataSource dataSource;

    public RegistroTarjetaRepositoryAdapter(DataSource dataSource) {
        this.dataSource = dataSource;
        initializeDatabase();
    }

    private void initializeDatabase() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS registro_tarjetas (
                id VARCHAR(36) PRIMARY KEY,
                nombre_cliente VARCHAR(255) NOT NULL,
                numero_celular VARCHAR(20) NOT NULL,
                marca VARCHAR(100) NOT NULL,
                modelo VARCHAR(100) NOT NULL,
                problema_descrito TEXT NOT NULL,
                tecnico_id VARCHAR(36) NOT NULL,
                tecnico_nombre VARCHAR(255) NOT NULL,
                estado VARCHAR(50) DEFAULT 'EN_PROGRESO',
                fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                fecha_finalizacion TIMESTAMP NULL,
                fecha_entrega TIMESTAMP NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("✅ Tabla 'registro_tarjetas' inicializada");
        } catch (SQLException e) {
            throw new RuntimeException("Error al inicializar tabla registro_tarjetas", e);
        }
    }

    @Override
    public RegistroTarjeta save(RegistroTarjeta tarjeta) {
        String sql = """
            INSERT INTO registro_tarjetas (
                id, nombre_cliente, numero_celular, marca, modelo,
                problema_descrito, tecnico_id, tecnico_nombre, estado,
                fecha_registro, created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarjeta.getId());
            stmt.setString(2, tarjeta.getNombreCliente());
            stmt.setString(3, tarjeta.getNumeroCelular());
            stmt.setString(4, tarjeta.getMarca());
            stmt.setString(5, tarjeta.getModelo());
            stmt.setString(6, tarjeta.getProblemaDescrito());
            stmt.setString(7, tarjeta.getTecnicoId());
            stmt.setString(8, tarjeta.getTecnicoNombre());
            stmt.setString(9, tarjeta.getEstado());
            stmt.setTimestamp(10, Timestamp.valueOf(tarjeta.getFechaRegistro()));
            stmt.setTimestamp(11, Timestamp.valueOf(tarjeta.getCreatedAt()));
            stmt.setTimestamp(12, Timestamp.valueOf(tarjeta.getUpdatedAt()));

            stmt.executeUpdate();
            return tarjeta;

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar registro de tarjeta", e);
        }
    }

    @Override
    public Optional<RegistroTarjeta> findById(String id) {
        String sql = "SELECT * FROM registro_tarjetas WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToTarjeta(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar registro de tarjeta", e);
        }
    }

    @Override
    public List<RegistroTarjeta> findAll() {
        String sql = "SELECT * FROM registro_tarjetas ORDER BY fecha_registro DESC";
        List<RegistroTarjeta> tarjetas = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                tarjetas.add(mapResultSetToTarjeta(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener registros de tarjetas", e);
        }

        return tarjetas;
    }

    @Override
    public List<RegistroTarjeta> findByTecnicoId(String tecnicoId) {
        String sql = "SELECT * FROM registro_tarjetas WHERE tecnico_id = ? ORDER BY fecha_registro DESC";
        List<RegistroTarjeta> tarjetas = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tecnicoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tarjetas.add(mapResultSetToTarjeta(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar registros por técnico", e);
        }

        return tarjetas;
    }

    @Override
    public List<RegistroTarjeta> findByEstado(String estado) {
        String sql = "SELECT * FROM registro_tarjetas WHERE estado = ? ORDER BY fecha_registro DESC";
        List<RegistroTarjeta> tarjetas = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estado);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tarjetas.add(mapResultSetToTarjeta(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar registros por estado", e);
        }

        return tarjetas;
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM registro_tarjetas WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Registro de tarjeta no encontrado");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar registro de tarjeta", e);
        }
    }

    @Override
    public RegistroTarjeta update(String id, RegistroTarjeta tarjeta) {
        String sql = """
            UPDATE registro_tarjetas 
            SET nombre_cliente = ?, numero_celular = ?, marca = ?, modelo = ?,
                problema_descrito = ?, estado = ?, updated_at = ?
            WHERE id = ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarjeta.getNombreCliente());
            stmt.setString(2, tarjeta.getNumeroCelular());
            stmt.setString(3, tarjeta.getMarca());
            stmt.setString(4, tarjeta.getModelo());
            stmt.setString(5, tarjeta.getProblemaDescrito());
            stmt.setString(6, tarjeta.getEstado());
            stmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(8, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Registro de tarjeta no encontrado");
            }

            tarjeta.setId(id);
            return tarjeta;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar registro de tarjeta", e);
        }
    }

    private RegistroTarjeta mapResultSetToTarjeta(ResultSet rs) throws SQLException {
        RegistroTarjeta tarjeta = new RegistroTarjeta();
        tarjeta.setId(rs.getString("id"));
        tarjeta.setNombreCliente(rs.getString("nombre_cliente"));
        tarjeta.setNumeroCelular(rs.getString("numero_celular"));
        tarjeta.setMarca(rs.getString("marca"));
        tarjeta.setModelo(rs.getString("modelo"));
        tarjeta.setProblemaDescrito(rs.getString("problema_descrito"));
        tarjeta.setTecnicoId(rs.getString("tecnico_id"));
        tarjeta.setTecnicoNombre(rs.getString("tecnico_nombre"));
        tarjeta.setEstado(rs.getString("estado"));
        tarjeta.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());

        Timestamp fechaFinalizacion = rs.getTimestamp("fecha_finalizacion");
        if (fechaFinalizacion != null) {
            tarjeta.setFechaFinalizacion(fechaFinalizacion.toLocalDateTime());
        }

        Timestamp fechaEntrega = rs.getTimestamp("fecha_entrega");
        if (fechaEntrega != null) {
            tarjeta.setFechaEntrega(fechaEntrega.toLocalDateTime());
        }

        tarjeta.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        tarjeta.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

        return tarjeta;
    }
}