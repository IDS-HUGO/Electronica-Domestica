package com.electronica.producto.infrastructure.adapter;

import com.electronica.producto.domain.model.Producto;
import com.electronica.producto.domain.port.ProductoRepositoryPort;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoRepositoryAdapter implements ProductoRepositoryPort {
    private final DataSource dataSource;

    public ProductoRepositoryAdapter(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Producto save(Producto producto) {
        String sql = """
            INSERT INTO productos (
                id, nombre_producto, categoria, cantidad_ohms, unidad,
                cantidad_piezas, precio_unitario, fecha_registro, created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getId());
            stmt.setString(2, producto.getNombreProducto());
            stmt.setString(3, producto.getCategoria());
            stmt.setBigDecimal(4, producto.getCantidadOhms());
            stmt.setString(5, producto.getUnidad());
            stmt.setInt(6, producto.getCantidadPiezas());
            stmt.setBigDecimal(7, producto.getPrecioUnitario());
            stmt.setTimestamp(8, Timestamp.valueOf(producto.getFechaRegistro()));
            stmt.setTimestamp(9, Timestamp.valueOf(producto.getCreatedAt()));
            stmt.setTimestamp(10, Timestamp.valueOf(producto.getUpdatedAt()));

            stmt.executeUpdate();
            return producto;

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar producto", e);
        }
    }

    @Override
    public Optional<Producto> findById(String id) {
        String sql = "SELECT * FROM productos WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToProducto(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar producto por ID", e);
        }
    }

    @Override
    public List<Producto> findAll() {
        String sql = "SELECT * FROM productos ORDER BY nombre_producto ASC";
        List<Producto> productos = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                productos.add(mapResultSetToProducto(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener productos", e);
        }

        return productos;
    }

    @Override
    public List<Producto> findByCategoria(String categoria) {
        String sql = "SELECT * FROM productos WHERE categoria = ? ORDER BY nombre_producto ASC";
        List<Producto> productos = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                productos.add(mapResultSetToProducto(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar productos por categoría", e);
        }

        return productos;
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM productos WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Producto no encontrado");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar producto", e);
        }
    }

    @Override
    public Producto update(String id, Producto producto) {
        String sql = """
            UPDATE productos 
            SET nombre_producto = ?, categoria = ?, cantidad_ohms = ?, unidad = ?,
                cantidad_piezas = ?, precio_unitario = ?, updated_at = ?
            WHERE id = ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombreProducto());
            stmt.setString(2, producto.getCategoria());
            stmt.setBigDecimal(3, producto.getCantidadOhms());
            stmt.setString(4, producto.getUnidad());
            stmt.setInt(5, producto.getCantidadPiezas());
            stmt.setBigDecimal(6, producto.getPrecioUnitario());
            stmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(8, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Producto no encontrado");
            }

            producto.setId(id);
            return producto;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar producto", e);
        }
    }

    @Override
    public List<Producto> findLowStock(int threshold) {
        String sql = "SELECT * FROM productos WHERE cantidad_piezas < ? ORDER BY cantidad_piezas ASC";
        List<Producto> productos = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, threshold);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                productos.add(mapResultSetToProducto(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar productos con stock bajo", e);
        }

        return productos;
    }

    private Producto mapResultSetToProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setId(rs.getString("id"));
        producto.setNombreProducto(rs.getString("nombre_producto"));
        producto.setCategoria(rs.getString("categoria"));
        producto.setCantidadOhms(rs.getBigDecimal("cantidad_ohms"));
        producto.setUnidad(rs.getString("unidad"));
        producto.setCantidadPiezas(rs.getInt("cantidad_piezas"));
        producto.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
        producto.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
        producto.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        producto.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return producto;
    }
}