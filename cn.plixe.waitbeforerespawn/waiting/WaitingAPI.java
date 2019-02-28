package cn.plixe.waitbeforerespawn.waiting;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import cn.plixe.waitbeforerespawn.ConfigFiles;
import cn.plixe.waitbeforerespawn.Utils;

public class WaitingAPI {

	public static ArrayList<Player> playersWaitingList = new ArrayList<>();

	public static boolean waitingRoomDefined() {

		if (ConfigFiles.wRoomConf.contains("waiting-room")) {

			return true;

		} else {

			return false;

		}

	}

	public static void setRoom(Player player) {

		Location loc = player.getLocation();

		String world = loc.getWorld().getName();
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		float yaw = loc.getYaw();
		float pitch = loc.getPitch();

		ConfigFiles.wRoomConf.set("waiting-room.world", world);
		ConfigFiles.wRoomConf.set("waiting-room.x", x);
		ConfigFiles.wRoomConf.set("waiting-room.y", y);
		ConfigFiles.wRoomConf.set("waiting-room.z", z);
		ConfigFiles.wRoomConf.set("waiting-room.yaw", yaw);
		ConfigFiles.wRoomConf.set("waiting-room.pitch", pitch);

		ConfigFiles.saveWaitingRoomFile();

		Utils.sendColoredMessage(player, ConfigFiles.msgConf.getString("waiting-room-messages.set-room"));

	}

	public static void teleportToWaitingRoom(Player player) {

		if (waitingRoomDefined()) {

			World world = Bukkit.getWorld(ConfigFiles.wRoomConf.getString("waiting-room.world"));
			double x = ConfigFiles.wRoomConf.getDouble("waiting-room.x");
			double y = ConfigFiles.wRoomConf.getDouble("waiting-room.y");
			double z = ConfigFiles.wRoomConf.getDouble("waiting-room.z");
			float yaw = ConfigFiles.wRoomConf.getInt("waiting-room.yaw");
			float pitch = ConfigFiles.wRoomConf.getInt("waiting-room.pitch");

			Location loc = new Location(world, x, y, z, yaw, pitch);

			player.teleport(loc);

		}

	}

	public static String playerPath(Player player) {

		if (ConfigFiles.settingsConf.getBoolean("global-settings.crack-version")) {

			return player.getName();

		} else {

			return player.getUniqueId().toString();

		}

	}

	public static Location getRespawnLocation(Player player) {

		World world = Bukkit.getServer().getWorld(
				ConfigFiles.wSavesConf.getString("saves." + WaitingAPI.playerPath(player) + ".respawn-location.world"));
		Double x = ConfigFiles.wSavesConf.getDouble("saves." + WaitingAPI.playerPath(player) + ".respawn-location.x");
		Double y = ConfigFiles.wSavesConf.getDouble("saves." + WaitingAPI.playerPath(player) + ".respawn-location.y");
		Double z = ConfigFiles.wSavesConf.getDouble("saves." + WaitingAPI.playerPath(player) + ".respawn-location.z");
		Float yaw = (float) ConfigFiles.wSavesConf
				.getInt("saves." + WaitingAPI.playerPath(player) + ".respawn-location.yaw");
		Float pitch = (float) ConfigFiles.wSavesConf
				.getInt("saves." + WaitingAPI.playerPath(player) + ".respawn-location.pitch");

		Location location = new Location(world, x, y, z, yaw, pitch);

		return location;

	}

	public static Location getDeathLocation(Player player) {

		World world = Bukkit.getServer().getWorld(
				ConfigFiles.wSavesConf.getString("saves." + WaitingAPI.playerPath(player) + ".death-location.world"));
		Double x = ConfigFiles.wSavesConf.getDouble("saves." + WaitingAPI.playerPath(player) + ".death-location.x");
		Double y = ConfigFiles.wSavesConf.getDouble("saves." + WaitingAPI.playerPath(player) + ".death-location.y");
		Double z = ConfigFiles.wSavesConf.getDouble("saves." + WaitingAPI.playerPath(player) + ".death-location.z");
		Float yaw = (float) ConfigFiles.wSavesConf
				.getInt("saves." + WaitingAPI.playerPath(player) + ".death-location.yaw");
		Float pitch = (float) ConfigFiles.wSavesConf
				.getInt("saves." + WaitingAPI.playerPath(player) + ".death-location.pitch");

		Location location = new Location(world, x, y, z, yaw, pitch);

		return location;

	}
	
	public static void sendAfterNotifications(Player player) {

		if (ConfigFiles.settingsConf.getBoolean("messages-settings.after-message.chat-message.enable")) {

			if (ConfigFiles.settingsConf.getBoolean("messages-settings.after-message.chat-message.space")) {

				player.sendMessage("");
				Utils.sendColoredMessage(player, ConfigFiles.msgConf.getString("waiting-messages.after"));
				player.sendMessage("");

			} else {

				Utils.sendColoredMessage(player, ConfigFiles.msgConf.getString("waiting-messages.after"));

			}

		}

		if (ConfigFiles.settingsConf.getBoolean("messages-settings.after-message.bar-message")) {

			Utils.sendActionBar(player, ConfigFiles.msgConf.getString("waiting-messages.after"));

		}

		if (ConfigFiles.settingsConf.getBoolean("messages-settings.after-message.title-message")) {

			Utils.sendTitle(player, "", ConfigFiles.msgConf.getString("waiting-messages.after"));

		}

	}

	public static void sendWaitingNotifications(Player player, int counter) {

		if (ConfigFiles.settingsConf.getBoolean("messages-settings.chat-message.enable")) {

			if (ConfigFiles.settingsConf.getBoolean("messages-settings.chat-message.space")) {

				player.sendMessage("");
				Utils.sendColoredMessage(player,
						ConfigFiles.msgConf.getString("waiting-messages.countdown").replace("<seconds>", "" + counter));
				player.sendMessage("");

			} else {

				Utils.sendColoredMessage(player,
						ConfigFiles.msgConf.getString("waiting-messages.countdown").replace("<seconds>", "" + counter));

			}

		}

		if (ConfigFiles.settingsConf.getBoolean("messages-settings.bar-message")) {

			Utils.sendActionBar(player,
					ConfigFiles.msgConf.getString("waiting-messages.countdown").replace("<seconds>", "" + counter));

		}

		if (ConfigFiles.settingsConf.getBoolean("messages-settings.title-message")) {

			Utils.sendTitle(player,
					ConfigFiles.msgConf.getString("waiting-messages.title.top").replace("<seconds>", "" + counter),
					ConfigFiles.msgConf.getString("waiting-messages.title.subtitle").replace("<seconds>",
							"" + counter));

		}

		if (ConfigFiles.settingsConf.getBoolean("sounds-settings.enable")) {

			player.playSound(player.getLocation(),
					Sound.valueOf(ConfigFiles.settingsConf.getString("sounds-settings.name")), 1, 1);

		}

	}

}
