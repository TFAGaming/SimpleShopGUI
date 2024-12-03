# SimpleShopGUI

**SimpleShopGUI** is a simple and user-friendly Minecraft shop plugin based on GUIs. Players can browse a wide range of items, each organized into specific categories and types, making finding exactly what they're looking for easier.

Supported database providers: **SQLite**, **PostgreSQL**, **MySQL**

YouTube video: https://www.youtube.com/watch?v=2oxVJxw8HZE

## Features

- Open-source
- User-friendly
- Organized Categories and Types
- Permissions and Limits for Player Ranks

## Requirements

- [**Vault**](https://www.spigotmc.org/resources/vault.34315/)
- [**EssentialsX**](https://essentialsx.net/downloads.html)*

\*: **EssentialsX** or any other economy plugins that **Vault** currently supports.

## How to install
### From GitHub
Go to the [releases section](https://github.com/TFAGaming/SimpleShopGUI/releases), scroll down to find the version you want to install, click on **Assets** and then click on the **.jar** file.

### From Spigot
Click on the **Download Now** button to download the latest version of the plugin. If you want to use older versions, [click here](https://www.spigotmc.org/resources/simpleshopgui.119478/updates).

After the download completes, copy the **.jar** file, navigate to the plugins folder within your Minecraft server directory, and paste the file there. If your Minecraft server is currently running, you can use the command `/reload` to activate the plugin. To avoid any future problems, we advise stopping the server and then restarting it for a clean startup.

## How it works

The plugin allows players to sell their items with a fixed price, and other players chooses to buy the items or not. Each item in the shop has its maximum duration; by default, each item expires after 7 days. You can change the duration in the **config.yml** file if you want.

Expired items are not gone forever, they will stay as an item in the shop, but nobody can buy them. The sellers of the expired items can get their items back using the command `/listed`.

## Commands

- `/shop (category)`: Opens a GUI with all available categories in the shop.
- `/listed`: View all items that you are currently selling, including the expired items.
- `/sell [price]`: Sell an item that you are currently holding with your hand.

## Contributing
Feel free to fork the repository and submit a new pull request if you wish to contribute to this project.

Before you submit a pull request, ensure you tested it and have no issues. Also, keep the same coding style, which means don't use many unnecessary spaces or tabs.

## License

The **MIT** License.