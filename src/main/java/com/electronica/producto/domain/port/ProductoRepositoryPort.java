package com.electronica.producto.domain.port;

import com.electronica.producto.domain.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoRepositoryPort {
    Producto save(Producto producto);
    Optional<Producto> findById(String id);
    List<Producto> findAll();
    List<Producto> findByCategoria(String categoria);
    void deleteById(String id);
    Producto update(String id, Producto producto);
    List<Producto> findLowStock(int threshold);
}