package com.electronica.finalizado.domain.port;

import com.electronica.finalizado.domain.model.RegistroFinalizado;
import java.util.List;
import java.util.Optional;

public interface RegistroFinalizadoRepositoryPort {
    RegistroFinalizado save(RegistroFinalizado finalizado);
    Optional<RegistroFinalizado> findById(String id);
    List<RegistroFinalizado> findAll();
    List<RegistroFinalizado> findByTecnicoId(String tecnicoId);
    void deleteById(String id);
    RegistroFinalizado update(String id, RegistroFinalizado finalizado);
}