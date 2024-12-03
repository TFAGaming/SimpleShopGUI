package simpleshopgui.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import simpleshopgui.Plugin;
import simpleshopgui.managers.ShopDatabaseManager;

public class Database {
    public Connection connection = null;
    private final String provider;
    private final Plugin plugin;

    public Database(String provider, Plugin plugin) {
        this.provider = provider;
        this.plugin = plugin;
    }

    public Connection getConnection() throws SQLException {
        if (this.connection == null) {
            if (provider.equalsIgnoreCase("sqlite")) {
                String sqlitePath = "";

                if (provider.equalsIgnoreCase("sqlite")) {
                    sqlitePath = this.plugin.getDataFolder().getAbsolutePath() + "/"
                            + this.plugin.getConfig().getString("database.sqlite.path");
                }

                String url = "jdbc:sqlite:" + sqlitePath;

                Connection connection = DriverManager.getConnection(url);
                this.connection = connection;
            } else if (provider.equalsIgnoreCase("postgresql")) {
                String host = Plugin.config.getString("database.postgresql.host");
                int port = Plugin.config.getInt("database.postgresql.port");
                String database = Plugin.config.getString("database.postgresql.database");
                String username = Plugin.config.getString("database.postgresql.username");
                String password = Plugin.config.getString("database.postgresql.password");

                String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;

                Connection connection = DriverManager.getConnection(url, username, password);
                this.connection = connection;
            } else if (provider.equalsIgnoreCase("mysql")) {
                String host = Plugin.config.getString("database.mysql.host");
                int port = Plugin.config.getInt("database.mysql.port");
                String database = Plugin.config.getString("database.mysql.database");
                String username = Plugin.config.getString("database.mysql.username");
                String password = Plugin.config.getString("database.mysql.password");

                String url = "jdbc:mysql://" + host + ":" + port + "/" + database;

                Connection connection = DriverManager.getConnection(url, username, password);
                this.connection = connection;
            } else {
                throw new SQLException(
                        "Invalid provider \"" + provider + "\", valid providers: sqlite, postgresql, mysql");
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
        if (provider.equalsIgnoreCase("sqlite")) {
            executeStatement("CREATE TABLE IF NOT EXISTS shop_items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "player_uuid VARCHAR(36) NOT NULL," +
                    "item_data TEXT NOT NULL," +
                    "price BIGINT NOT NULL," +
                    "created_at BIGINT NOT NULL," +
                    "expires_at BIGINT NOT NULL," +
                    "category INTEGER NOT NULL" +
                    ");");
        } else if (provider.equalsIgnoreCase("postgresql")) {
            executeStatement("CREATE TABLE IF NOT EXISTS shop_items (" +
                    "id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
                    "player_uuid VARCHAR(36) NOT NULL," +
                    "item_data TEXT NOT NULL," +
                    "price BIGINT NOT NULL," +
                    "created_at BIGINT NOT NULL," +
                    "expires_at BIGINT NOT NULL," +
                    "category INTEGER NOT NULL" +
                    ");");
        } else if (provider.equalsIgnoreCase("mysql")) {
            executeStatement("CREATE TABLE IF NOT EXISTS shop_items (" +
                    "id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY," +
                    "player_uuid VARCHAR(36) NOT NULL," +
                    "item_data TEXT NOT NULL," +
                    "price BIGINT NOT NULL," +
                    "created_at BIGINT NOT NULL," +
                    "expires_at BIGINT NOT NULL," +
                    "category TINYINT UNSIGNED NOT NULL" +
                    ");");
        }

        ShopDatabaseManager.updateCache();
    }

    private void executeStatement(String sql) throws SQLException {
        Statement statement = getConnection().createStatement();

        statement.execute(sql);
        statement.close();
    }
}
