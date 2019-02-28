package cn.plixe.waitbeforerespawn.waiting.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import cn.plixe.waitbeforerespawn.ConfigFiles;
import cn.plixe.waitbeforerespawn.Utils;
import cn.plixe.waitbeforerespawn.waiting.WaitingAPI;

public class WaitingBlockEvent implements Listener {

	@EventHandler
	public void onWaitingBreakBlock(BlockBreakEvent e) {

		Player player = e.getPlayer();

		if (WaitingAPI.playersWaitingList.contains(player)) {

			if (ConfigFiles.settingsConf.getBoolean("global-settings.break-blocks")) {

				return;

			} else {

				Utils.sendColoredMessage(player, ConfigFiles.msgConf.getString("waiting-messages.break-blocks"));
				e.setCancelled(true);

			}

		}

	}

	@EventHandler
	public void onWaitingPlaceBlock(BlockPlaceEvent e) {

		Player player = e.getPlayer();

		if (WaitingAPI.playersWaitingList.contains(player)) {

			if (ConfigFiles.settingsConf.getBoolean("global-settings.place-blocks")) {

				return;

			} else {

				Utils.sendColoredMessage(player, ConfigFiles.msgConf.getString("waiting-messages.place-blocks"));
				e.setCancelled(true);

			}

		}

	}

}
