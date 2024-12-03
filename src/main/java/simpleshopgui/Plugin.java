package simpleshopgui;

import java.sql.SQLException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import simpleshopgui.commands.ListedCommand;
import simpleshopgui.commands.SellCommand;
import simpleshopgui.commands.ShopCommand;
import simpleshopgui.database.Database;
import simpleshopgui.events.gui.GUIListener;
import simpleshopgui.events.gui.PaginationGUIListener;
import simpleshopgui.integrations.Vault;
import simpleshopgui.utils.console.Console;

public class Plugin extends JavaPlugin {
	public static Database database;
	public static Vault vaultapi;
	public static FileConfiguration config;

	public void onEnable() {
		Console.pluginBanner();
		//Console.warning("Enabling the plugin...");

		saveDefaultConfig();

		vaultapi = new Vault(this);

		if (!Plugin.vaultapi.setupPermissions()) {
			Console.error("Failed to load permissions from Vault, disabling...");
			disablePlugin();
			return;
		} else {
			Console.info("Successfully loaded Vault permissions.");
		}

		if (!Plugin.vaultapi.setupEconomy()) {
			Console.error("Failed to load economy from Vault., disabling...");
			disablePlugin();
			return;
		} else {
			Console.info("Successfully loaded Vault economy.");
		}

		if (!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}

		config = getConfig();

		String provider = config.getString("database.provider");

		database = new Database(config.getString("database.provider"), this);

		try {
			database.getConnection();
			database.prepareTables();

			Console.info("Successfully connected to the database (provider: " + provider + ").");
		} catch (SQLException e) {
			e.printStackTrace();
			Console.error("Failed to connect to the database (provider: " + provider + "), disabling...");
			disablePlugin();
		}

		getServer().getPluginManager().registerEvents(new GUIListener(), this);
		getServer().getPluginManager().registerEvents(new PaginationGUIListener(), this);

		getCommand("shop").setExecutor(new ShopCommand());
		getCommand("sell").setExecutor(new SellCommand());
		getCommand("listed").setExecutor(new ListedCommand());

		Console.info("The plugin is now enabled.");
	}

	public void onDisable() {
		//Console.warning("Disabling the plugin...");
		Console.info("SimpleShopGUI is now disabled.");
	}

	public static String getVersion() {
		return "1.2.0";
	}

	public void disablePlugin() {
		try {
			database.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		getServer().getPluginManager().disablePlugin(this);
	}
}
