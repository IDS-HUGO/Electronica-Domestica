package com.electronica.finalizado.repositories;

import com.electronica.finalizado.models.RegistroFinalizado;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FinalizadoRepository {
    private final DataSource dataSource;

    public FinalizadoRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        initializeDatabase();
    }

    private void initializeDatabase() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS registro_finalizado (
                id VARCHAR(36) PRIMARY KEY,
                registro_tarjeta_id VARCHAR(36) NOT NULL,
                nombre_cliente VARCHAR(255) NOT NULL,
                numero_celular VARCHAR(20) NOT NULL,
                marca VARCHAR(100) NOT NULL,
                modelo VARCHAR(100) NOT NULL,
                problema_cambiado TEXT NOT NULL,
                tecnico_id VARCHAR(36) NOT NULL,
                tecnico_nombre VARCHAR(255) NOT NULL,
                fecha_entrega TIMESTAMP NOT NULL,
                costo_reparacion DECIMAL(10,2) DEFAULT 0.00,
                fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("✅ Tabla 'registro_finalizado' inicializada");
        } catch (SQLException e) {
            throw new RuntimeException("Error al inicializar tabla registro_finalizado", e);
        }
    }

    public RegistroFinalizado save(RegistroFinalizado finalizado) {
        String sql = """
            INSERT INTO registro_finalizado (
                id, registro_tarjeta_id, nombre_cliente, numero_celular,
                marca, modelo, problema_cambiado, tecnico_id, tecnico_nombre,
                fecha_entrega, costo_reparacion, fecha_registro, created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, finalizado.getId());
            stmt.setString(2, finalizado.getRegistroTarjetaId());
            stmt.setString(3, finalizado.getNombreCliente());
            stmt.setString(4, finalizado.getNumeroCelular());
            stmt.setString(5, finalizado.getMarca());
            stmt.setString(6, finalizado.getModelo());
            stmt.setString(7, finalizado.getProblemaCambiado());
            stmt.setString(8, finalizado.getTecnicoId());
            stmt.setString(9, finalizado.getTecnicoNombre());
            stmt.setTimestamp(10, Timestamp.valueOf(finalizado.getFechaEntrega()));
            stmt.setBigDecimal(11, finalizado.getCostoReparacion());
            stmt.setTimestamp(12, Timestamp.valueOf(finalizado.getFechaRegistro()));
            stmt.setTimestamp(13, Timestamp.valueOf(finalizado.getCreatedAt()));
            stmt.setTimestamp(14, Timestamp.valueOf(finalizado.getUpdatedAt()));

            stmt.executeUpdate();
            return finalizado;

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar registro finalizado", e);
        }
    }

    public Optional<RegistroFinalizado> findById(String id) {
        String sql = "SELECT * FROM registro_finalizado WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToFinalizado(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar registro finalizado", e);
        }
    }

    public List<RegistroFinalizado> findAll() {
        String sql = "SELECT * FROM registro_finalizado ORDER BY fecha_entrega DESC";
        List<RegistroFinalizado> finalizados = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                finalizados.add(mapResultSetToFinalizado(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener registros finalizados", e);
        }

        return finalizados;
    }

    public List<RegistroFinalizado> findByTecnicoId(String tecnicoId) {
        String sql = "SELECT * FROM registro_finalizado WHERE tecnico_id = ? ORDER BY fecha_entrega DESC";
        List<RegistroFinalizado> finalizados = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tecnicoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                finalizados.add(mapResultSetToFinalizado(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar registros por técnico", e);
        }

        return finalizados;
    }

    public void deleteById(String id) {
        String sql = "DELETE FROM registro_finalizado WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Registro finalizado no encontrado");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar registro finalizado", e);
        }
    }

    public RegistroFinalizado update(String id, RegistroFinalizado finalizado) {
        String sql = """
            UPDATE registro_finalizado 
            SET problema_cambiado = ?, fecha_entrega = ?, costo_reparacion = ?, updated_at = ?
            WHERE id = ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, finalizado.getProblemaCambiado());
            stmt.setTimestamp(2, Timestamp.valueOf(finalizado.getFechaEntrega()));
            stmt.setBigDecimal(3, finalizado.getCostoReparacion());
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(5, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Registro finalizado no encontrado");
            }

            finalizado.setId(id);
            return finalizado;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar registro finalizado", e);
        }
    }

    private RegistroFinalizado mapResultSetToFinalizado(ResultSet rs) throws SQLException {
        RegistroFinalizado finalizado = new RegistroFinalizado();
        finalizado.setId(rs.getString("id"));
        finalizado.setRegistroTarjetaId(rs.getString("registro_tarjeta_id"));
        finalizado.setNombreCliente(rs.getString("nombre_cliente"));
        finalizado.setNumeroCelular(rs.getString("numero_celular"));
        finalizado.setMarca(rs.getString("marca"));
        finalizado.setModelo(rs.getString("modelo"));
        finalizado.setProblemaCambiado(rs.getString("problema_cambiado"));
        finalizado.setTecnicoId(rs.getString("tecnico_id"));
        finalizado.setTecnicoNombre(rs.getString("tecnico_nombre"));
        finalizado.setFechaEntrega(rs.getTimestamp("fecha_entrega").toLocalDateTime());
        finalizado.setCostoReparacion(rs.getBigDecimal("costo_reparacion"));
        finalizado.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
        finalizado.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        finalizado.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return finalizado;
    }
}