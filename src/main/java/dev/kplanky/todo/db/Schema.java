package dev.kplanky.todo.db;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Applies db/schema.sql at startup so a fresh database (a new container volume, say) comes up
 * usable without anyone running psql by hand. The script is written with IF NOT EXISTS
 * throughout, so running it on every boot is a no-op once the tables exist.
 */
public final class Schema {

    private static final String RESOURCE = "/db/schema.sql";

    private Schema() {
    }

    public static void apply(DataSource dataSource) {
        String sql = read();
        try (Connection c = dataSource.getConnection();
             Statement s = c.createStatement()) {
            s.execute(sql);
            System.out.println("Schema applied from " + RESOURCE);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot apply " + RESOURCE, e);
        }
    }

    private static String read() {
        try (InputStream in = Schema.class.getResourceAsStream(RESOURCE)) {
            if (in == null) {
                throw new IllegalStateException("Missing " + RESOURCE + " on the classpath");
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read " + RESOURCE, e);
        }
    }

}
