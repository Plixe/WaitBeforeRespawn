package cn.plixe.waitbeforerespawn.waiting.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import cn.plixe.waitbeforerespawn.ConfigFiles;
import cn.plixe.waitbeforerespawn.Utils;
import cn.plixe.waitbeforerespawn.waiting.WaitingAPI;

public class WaitingQuitEvent implements Listener {

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {

		Player player = e.getPlayer();

		if (WaitingAPI.playersWaitingList.contains(player)) {

			Utils.sendColoredMessage(Bukkit.getConsoleSender(),
					ConfigFiles.msgConf.getString("anti-unlogin-message").replace("<player>", player.getName()));

			WaitingAPI.playersWaitingList.remove(player);

		}

	}

}
