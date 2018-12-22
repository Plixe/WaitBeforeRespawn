package cn.plixe.waitbeforerespawn;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import cn.plixe.waitbeforerespawn.waiting.WaitingCommands;
import cn.plixe.waitbeforerespawn.waiting.Events.WaitingChatEvent;
import cn.plixe.waitbeforerespawn.waiting.Events.WaitingCommandsEvent;
import cn.plixe.waitbeforerespawn.waiting.Events.WaitingMoveEvent;
import cn.plixe.waitbeforerespawn.waiting.Events.WaitingQuitEvent;
import cn.plixe.waitbeforerespawn.waiting.Events.WaitingRespawnEvent;

public class Main extends JavaPlugin {

	public static Plugin instance;

	public void onEnable() {

		instance = this;
		initEvents();
		ConfigFiles.createFiles();

		getCommand("wbr").setExecutor(new WaitingCommands());

	}

	public void initEvents() {

		PluginManager pm = Bukkit.getServer().getPluginManager();

		pm.registerEvents(new WaitingRespawnEvent(), instance);
		pm.registerEvents(new WaitingMoveEvent(), instance);
		pm.registerEvents(new WaitingCommandsEvent(), instance);
		pm.registerEvents(new WaitingChatEvent(), instance);
		pm.registerEvents(new WaitingQuitEvent(), instance);

	}

}
