package cn.plixe.waitbeforerespawn.waiting.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import cn.plixe.waitbeforerespawn.ConfigFiles;
import cn.plixe.waitbeforerespawn.waiting.WaitingAPI;

public class WaitingChatEvent implements Listener {

	@EventHandler
	public void chatPlayerEvent(AsyncPlayerChatEvent e) {

		if (WaitingAPI.playersWaitingList.contains(e.getPlayer())) {

			if (ConfigFiles.settingsConf.getBoolean("global-settings.player-chat")) {

				e.setCancelled(false);

			} else {

				e.setCancelled(true);

			}

		}

	}

}
