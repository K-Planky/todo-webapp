package dev.kplanky.todo.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public final class Database {

    private Database() {
    }

    public static DataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(required("DB_URL"));
        config.setUsername(required("DB_USER"));
        config.setPassword(required("DB_PASSWORD"));
        config.setMaximumPoolSize(10);
        config.setPoolName("todo-pool");
        return new HikariDataSource(config);
    }

    private static String required(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required environment variable " + name);
        }
        return value;
    }

}
