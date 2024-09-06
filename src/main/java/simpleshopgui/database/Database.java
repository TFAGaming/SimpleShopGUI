package simpleshopgui.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import simpleshopgui.managers.ShopDatabaseManager;

public class Database {
    public Connection connection = null;
    private final String provider;
    private final String jdbcUrl;

    public Database(String provider, String jdbcUrl) {
        this.provider = provider;
        this.jdbcUrl = jdbcUrl;
    }

    public Connection getConnection() throws SQLException {
        if (this.connection == null) {
            if (provider.equalsIgnoreCase("sqlite")) {
                Connection connection = DriverManager.getConnection("jdbc:sqlite:" + jdbcUrl);
                this.connection = connection;
            } else {
                throw new SQLException("Invalid provider \"" + provider + "\"");
            }
        }

        return this.connection;
    }

    public void closeConnection() throws SQLException {
        if (this.connection != null && !connection.isClosed()) {
            this.connection.close();
            this.connection = null;
        }
    }

    public void prepareTables() throws SQLException {
        executeStatement("CREATE TABLE IF NOT EXISTS sold_items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "player_uuid VARCHAR(36) NOT NULL," +
                "item_data TEXT NOT NULL," +
                "price BIGINT NOT NULL," +
                "created_at BIGINT NOT NULL," +
                "expires BIGINT NOT NULL," +
                "category TEXT NOT NULL" +
                ");");

        ShopDatabaseManager.updateCache();
    }

    private void executeStatement(String sql) throws SQLException {
        Statement statement = getConnection().createStatement();

        statement.execute(sql);
        statement.close();
    }
}
