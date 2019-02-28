package cn.plixe.waitbeforerespawn;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import cn.plixe.waitbeforerespawn.waiting.WaitingCommands;
import cn.plixe.waitbeforerespawn.waiting.Events.WaitingBlockEvent;
import cn.plixe.waitbeforerespawn.waiting.Events.WaitingChatEvent;
import cn.plixe.waitbeforerespawn.waiting.Events.WaitingCommandsEvent;
import cn.plixe.waitbeforerespawn.waiting.Events.WaitingDeathEvent;
import cn.plixe.waitbeforerespawn.waiting.Events.WaitingJoinEvent;
import cn.plixe.waitbeforerespawn.waiting.Events.WaitingMoveEvent;
import cn.plixe.waitbeforerespawn.waiting.Events.WaitingQuitEvent;
import cn.plixe.waitbeforerespawn.waiting.Events.WaitingRespawnEvent;
import cn.plixe.waitbeforerespawn.waiting.Events.WaitingTeleportEvent;

public class Main extends JavaPlugin {

	public static Plugin instance;

	public void onEnable() {

		instance = this;
		initEvents();
		ConfigFiles.createFiles();

		getCommand("wbr").setExecutor(new WaitingCommands());

		if (ConfigFiles.settingsConf.getBoolean("form-message")) {

			BukkitTask countdownTask = new BukkitRunnable() {

				public void run() {

					Utils.sendColoredMessage(Bukkit.getConsoleSender(), "");
					Utils.sendColoredMessage(Bukkit.getConsoleSender(),
							"&a&l[WaitBeforeRespawn] : &aHelp me to make this plugin better with this form https://goo.gl/forms/ut7kLgNylNb2AjlA2");
					Utils.sendColoredMessage(Bukkit.getConsoleSender(),
							"&eOnly admin can see this message. Disable it on settings.yml file. (see last line)");
					Utils.sendColoredMessage(Bukkit.getConsoleSender(), "&eSorry for the trouble - Plixe.");
					Utils.sendColoredMessage(Bukkit.getConsoleSender(), "");
					
					cancel();

				}

			}.runTaskTimer(Main.instance, 20 * 10, 20);

		}

	}

	public void initEvents() {

		PluginManager pm = Bukkit.getServer().getPluginManager();

		pm.registerEvents(new WaitingRespawnEvent(), instance);
		pm.registerEvents(new WaitingMoveEvent(), instance);
		pm.registerEvents(new WaitingCommandsEvent(), instance);
		pm.registerEvents(new WaitingChatEvent(), instance);
		pm.registerEvents(new WaitingQuitEvent(), instance);
		pm.registerEvents(new WaitingJoinEvent(), instance);
		pm.registerEvents(new WaitingDeathEvent(), instance);
		pm.registerEvents(new WaitingTeleportEvent(), instance);
		pm.registerEvents(new WaitingBlockEvent(), instance);

	}

}
