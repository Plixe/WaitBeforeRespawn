package cn.plixe.waitbeforerespawn.waiting;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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

}
