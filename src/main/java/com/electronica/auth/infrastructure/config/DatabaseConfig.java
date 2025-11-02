package com.electronica.auth.infrastructure.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DatabaseConfig {

    public static DataSource createDataSource() {
        HikariConfig config = new HikariConfig();

        // Configuración para H2 (base de datos en memoria para desarrollo)
        config.setJdbcUrl("jdbc:h2:./data/electronica_db;AUTO_SERVER=TRUE");
        config.setUsername("sa");
        config.setPassword("");
        config.setDriverClassName("org.h2.Driver");

        // Configuraciones de pool
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        return new HikariDataSource(config);
    }
}