package simpleshopgui.utils.console;

import java.util.logging.Logger;

import simpleshopgui.Plugin;
import simpleshopgui.utils.colors.ConsoleColors;

public class Console {
    private static final Logger logger = Logger.getLogger("SimpleShopGUI");

    public static void pluginBanner() {
        String line_splitter = "";

        for (int i = 0; i < 74; i++) {
            line_splitter += "-";
        }

        String banner = "  ____  _                 _      ____  _                  ____ _   _ ___ \r\n" +
                        " / ___|(_)_ __ ___  _ __ | | ___/ ___|| |__   ___  _ __  / ___| | | |_ _|\r\n" +
                        " \\___ \\| | '_ ` _ \\| '_ \\| |/ _ \\___ \\| '_ \\ / _ \\| '_ \\| |  _| | | || | \r\n" +
                        "  ___) | | | | | | | |_) | |  __/___) | | | | (_) | |_) | |_| | |_| || | \r\n" +
                        " |____/|_|_| |_| |_| .__/|_|\\___|____/|_| |_|\\___/| .__/ \\____|\\___/|___|\r\n" +
                        "                   |_|                            |_|                    " +
                        "\nCreated by TFAGaming, Version: v" + Plugin.getVersion();

        logger.info(line_splitter);

        for (String line : banner.split("\n")) {
            logger.info(line);
        }

        logger.info(line_splitter);
    }

    public static void info(String message) {
        logger.info(message);

        return;
    }

    public static void warning(String message) {
        logger.warning(ConsoleColors.YELLOW + message + ConsoleColors.RESET);

        return;
    }

    public static void error(String message) {
        logger.severe(ConsoleColors.RED + message + ConsoleColors.RESET);

        return;
    }
}
