package cn.plixe.waitbeforerespawn;

import java.lang.reflect.Constructor;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Utils {

	public static String coloredString(String str) {

		return ChatColor.translateAlternateColorCodes('&', str);

	}

	public static void sendColoredMessage(CommandSender sender, String msg) {

		sender.sendMessage(coloredString(msg));

	}

	public static void sendNoPermMessage(CommandSender sender) {

		sendColoredMessage(sender, ConfigFiles.msgConf.getString("global-messages.no-permissions"));

	}

	public static void sendActionBar(Player player, String msg) {

		String s = ChatColor.translateAlternateColorCodes('&', msg);

		try {

			Constructor<?> constructor = getNMSClass("PacketPlayOutChat")
					.getConstructor(getNMSClass("IChatBaseComponent"), getNMSClass("ChatMessageType"));

			Object icbc = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class)
					.invoke(null, "{\"text\":\"" + s + "\"}");
			Object packet = constructor.newInstance(icbc, getNMSClass("ChatMessageType").getEnumConstants()[2]);
			Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);

			playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	public static void sendPacket(Player player, Object packet) {
		try {

			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	public static void sendTitle(Player player, String title, String subtitle) {

		String t = ChatColor.translateAlternateColorCodes('&', title);
		String s = ChatColor.translateAlternateColorCodes('&', subtitle);

		try {

			Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class)
					.invoke(null, "{\"text\": \"" + t + "\"}");
			Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(
					getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"),
					int.class, int.class, int.class);
			Object packet = titleConstructor.newInstance(
					getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null), chatTitle, 0,
					20, 0);

			Object chatSubTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class)
					.invoke(null, "{\"text\": \"" + s + "\"}");
			Constructor<?> stitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(
					getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"),
					int.class, int.class, int.class);
			Object spacket = stitleConstructor.newInstance(
					getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null),
					chatSubTitle, 0, 20, 0);

			sendPacket(player, packet);
			sendPacket(player, spacket);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public static Class<?> getNMSClass(String name) {

		try {

			return Class.forName("net.minecraft.server." + getVersion() + "." + name);

		} catch (ClassNotFoundException e) {

			e.printStackTrace();

			return null;

		}

	}

	public static String getVersion() {

		return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

	}

}
