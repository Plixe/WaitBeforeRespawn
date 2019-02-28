package cn.plixe.waitbeforerespawn.waiting.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import cn.plixe.waitbeforerespawn.ConfigFiles;
import cn.plixe.waitbeforerespawn.Utils;

public class WaitingTeleportEvent implements Listener {

	@EventHandler
	public void onWaitingTeleport(PlayerTeleportEvent e) {

		if (ConfigFiles.settingsConf.getBoolean("spectator-mode.enable")
				&& ConfigFiles.settingsConf.getBoolean("spectator-mode.player-teleportation")
				&& !Utils.getVersion().contains("1.7")) {

			if (e.getCause() == TeleportCause.valueOf("SPECTATE")) {

				Utils.sendColoredMessage(e.getPlayer(), ConfigFiles.msgConf.getString("spectator-messages.player-teleportation"));
				e.setCancelled(true);

			} else {

				return;

			}

		}

	}

}
