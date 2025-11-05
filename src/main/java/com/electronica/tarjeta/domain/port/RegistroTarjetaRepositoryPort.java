package com.electronica.tarjeta.domain.port;

import com.electronica.tarjeta.domain.model.RegistroTarjeta;
import java.util.List;
import java.util.Optional;

public interface RegistroTarjetaRepositoryPort {
    RegistroTarjeta save(RegistroTarjeta tarjeta);
    Optional<RegistroTarjeta> findById(String id);
    List<RegistroTarjeta> findAll();
    List<RegistroTarjeta> findByTecnicoId(String tecnicoId);
    List<RegistroTarjeta> findByEstado(String estado);
    void deleteById(String id);
    RegistroTarjeta update(String id, RegistroTarjeta tarjeta);
}