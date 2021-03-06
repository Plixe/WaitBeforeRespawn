package cn.plixe.waitbeforerespawn;

import java.io.File;
import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigFiles {

	public static File settingsFile, msgFile, wRoomFile, wSavesFile;
	public static FileConfiguration settingsConf, msgConf, wRoomConf, wSavesConf;

	public static void createFiles() {

		settingsFile = new File(Main.instance.getDataFolder(), "settings.yml");
		msgFile = new File(Main.instance.getDataFolder(), "messages.yml");
		wRoomFile = new File(Main.instance.getDataFolder(), "waiting-room.yml");
		wSavesFile = new File(Main.instance.getDataFolder(), "waiting-saves.yml");

		if (!settingsFile.exists()) {

			settingsFile.getParentFile().mkdirs();
			Main.instance.saveResource("settings.yml", false);

		}

		if (!msgFile.exists()) {

			msgFile.getParentFile().mkdirs();
			Main.instance.saveResource("messages.yml", false);

		}

		if (!wRoomFile.exists()) {

			wRoomFile.getParentFile().mkdirs();
			Main.instance.saveResource("waiting-room.yml", false);

		}

		if (!wSavesFile.exists()) {

			wSavesFile.getParentFile().mkdirs();
			Main.instance.saveResource("waiting-saves.yml", false);

		}

		try {

			settingsConf = new YamlConfiguration();
			settingsConf.load(settingsFile);

			msgConf = new YamlConfiguration();
			msgConf.load(msgFile);

			wRoomConf = new YamlConfiguration();
			wRoomConf.load(wRoomFile);

			wSavesConf = new YamlConfiguration();
			wSavesConf.load(wSavesFile);

		} catch (InvalidConfigurationException | IOException e) {

			e.printStackTrace();

		}

	}

	public static void saveSettingsFile() {

		try {

			settingsConf.save(settingsFile);

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	public static void saveMessagesFile() {

		try {

			msgConf.save(msgFile);
		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	public static void saveWaitingRoomFile() {

		try {

			wRoomConf.save(wRoomFile);

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	public static void saveWaitingSavesFile() {

		try {

			wSavesConf.save(wSavesFile);

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	public static void reloadFiles(CommandSender sender) {

		try {

			settingsConf.load(settingsFile);
			sender.sendMessage(Utils.coloredString("&asettings.yml correctly reloaded"));

		} catch (Exception e) {

			e.printStackTrace();
			sender.sendMessage(Utils.coloredString("&csettings.yml can't be reloaded. See logs."));

		}

		try {

			msgConf.load(msgFile);
			sender.sendMessage(Utils.coloredString("&amessages.yml correctly reloaded"));

		} catch (InvalidConfigurationException | IOException e) {

			e.printStackTrace();
			sender.sendMessage(Utils.coloredString("&cmessages.yml can't be reloaded. See logs."));

		}

		try {

			wRoomConf.load(wRoomFile);
			sender.sendMessage(Utils.coloredString("&awaiting-room.yml correctly reloaded"));

		} catch (InvalidConfigurationException | IOException e) {

			e.printStackTrace();
			sender.sendMessage(Utils.coloredString("&cwaiting-room.yml can't be reloaded. See logs."));

		}

		try {

			wSavesConf.load(wSavesFile);
			sender.sendMessage(Utils.coloredString("&awaiting-saves.yml correctly reloaded"));

		} catch (InvalidConfigurationException | IOException e) {

			e.printStackTrace();
			sender.sendMessage(Utils.coloredString("&cwaiting-saves.yml can't be reloaded. See logs."));

		}

	}

}
