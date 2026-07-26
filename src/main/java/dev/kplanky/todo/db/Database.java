package dev.kplanky.todo.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public final class Database {

    private Database() {
    }

    public static DataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl());
        config.setUsername(required("DB_USER"));
        config.setPassword(required("DB_PASSWORD"));
        config.setMaximumPoolSize(10);
        config.setPoolName("todo-pool");
        return new HikariDataSource(config);
    }

    /**
     * A whole DB_URL wins when it is set, which keeps local runs and the README unchanged.
     * Otherwise the URL is assembled from the parts the container platform injects
     * (DB_HOST, DB_PORT, DB_NAME), so the deployment needs no app-specific env vars.
     */
    private static String jdbcUrl() {
        String url = System.getenv("DB_URL");
        if (url != null && !url.isBlank()) {
            return url;
        }
        String host = System.getenv("DB_HOST");
        String name = System.getenv("DB_NAME");
        if (host == null || host.isBlank() || name == null || name.isBlank()) {
            throw new IllegalStateException(
                    "Set DB_URL, or set DB_HOST, DB_NAME and optionally DB_PORT");
        }
        String port = System.getenv("DB_PORT");
        if (port == null || port.isBlank()) {
            port = "5432";
        }
        return "jdbc:postgresql://" + host + ":" + port + "/" + name;
    }

    private static String required(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required environment variable " + name);
        }
        return value;
    }

}
