package com.acmeflix;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {
    private static final String DB_CONNECTION_URL_FILE_MODE = "jdbc:h2:~/sample";
    private static final String DB_CONNECTION_URL_MEMORY_MODE = "jdbc:h2:mem:sample;DB_CLOSE_DELAY=-1";
    private static final String DB_USERNAME = "sa";
    private static final String DB_PASSWORD = "";

    private static final HikariDataSource hikariDataSource;
    private static final HikariConfig config = new HikariConfig();

    static {
        // See https://github.com/brettwooldridge/HikariCP#gear-configuration-knobs-baby
        // for the complete list of configuration parameters

        config.setJdbcUrl(DB_CONNECTION_URL_MEMORY_MODE);
        config.setUsername(DB_USERNAME);
        config.setPassword(DB_PASSWORD);

        // This property controls the maximum number of milliseconds that a client (that's you) will wait for a
        // connection from the pool.
        // Defaults to 30000ms (30secs).
        config.setConnectionTimeout(10000);

        // This property controls the maximum amount of time that a connection is allowed to sit idle in the pool.
        // This setting only applies when minimumIdle is defined to be less than maximumPoolSize.
        // Defaults to 600000ms (10mins).
        config.setIdleTimeout(10000);

        // This property controls the maximum lifetime of a connection in the pool. An in-use connection will never be
        // retired, only when it is closed will it then be removed.
        // Defaults to 1800000ms (30mins).
        config.setMaxLifetime(1800000);

        // This property controls the minimum number of idle connections that HikariCP tries to maintain in the pool.
        // Defaults to maximumPoolSize value.
        config.setMinimumIdle(1);

        // This property controls the maximum size that the pool is allowed to reach, including both idle and in-use
        // connections.
        // Defaults to 10.
        config.setMaximumPoolSize(3);

        // This property controls the default auto-commit behavior of connections returned from the pool.
        // Defaults to true.
        config.setAutoCommit(true);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");

        hikariDataSource = new HikariDataSource(config);
    }

    private DataSource() {
    }

    public static Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }
}
