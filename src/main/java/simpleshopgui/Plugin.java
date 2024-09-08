package simpleshopgui;

import java.sql.SQLException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import simpleshopgui.commands.ListedCommand;
import simpleshopgui.commands.SellCommand;
import simpleshopgui.commands.ShopCommand;
import simpleshopgui.database.Database;
import simpleshopgui.events.gui.NormalGUIListener;
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

		config = Plugin.getPlugin(Plugin.class).getConfig();

		String provider = config.getString("database.provider");
		String jdbcUrl = "";

		if (provider.equalsIgnoreCase("sqlite")) {
			jdbcUrl = getDataFolder().getAbsolutePath() + "/" + config.getString("database.path");
		}

		database = new Database(config.getString("database.provider"), jdbcUrl);

		try {
			database.getConnection();
			database.prepareTables();

			Console.info("Successfully connected to the database.");
		} catch (SQLException e) {
			e.printStackTrace();
			Console.error("Failed to connect to the database, disabling...");
			disablePlugin();
		}

		getServer().getPluginManager().registerEvents(new NormalGUIListener(), this);
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
		return "1.0.0";
	}

	private void disablePlugin() {
		getServer().getPluginManager().disablePlugin(this);
	}
}
