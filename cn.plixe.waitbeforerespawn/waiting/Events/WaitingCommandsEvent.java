package cn.plixe.waitbeforerespawn.waiting.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import cn.plixe.waitbeforerespawn.ConfigFiles;
import cn.plixe.waitbeforerespawn.Utils;
import cn.plixe.waitbeforerespawn.waiting.WaitingAPI;

public class WaitingCommandsEvent implements Listener {

	@EventHandler
	public void sendCommandEvent(PlayerCommandPreprocessEvent e) {

		if (WaitingAPI.playersWaitingList.contains(e.getPlayer())) {

			Utils.sendColoredMessage(e.getPlayer(), ConfigFiles.msgConf.getString("waiting-messages.commands-disabled"));
			e.setCancelled(true);

		} else {

			e.setCancelled(false);

		}

	}

}
