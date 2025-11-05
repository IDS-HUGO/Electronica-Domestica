// UBICACIÓN: src/main/java/com/electronica/tarjeta/application/usecase/DeleteRegistroTarjetaUseCase.java
package com.electronica.tarjeta.application.usecase;

import com.electronica.tarjeta.domain.port.RegistroTarjetaRepositoryPort;

public class DeleteRegistroTarjetaUseCase {
    private final RegistroTarjetaRepositoryPort repository;

    public DeleteRegistroTarjetaUseCase(RegistroTarjetaRepositoryPort repository) {
        this.repository = repository;
    }

    public void execute(String id) {
        // Verificar que existe antes de eliminar
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro de tarjeta no encontrado"));

        // Eliminar
        repository.deleteById(id);
    }
}
