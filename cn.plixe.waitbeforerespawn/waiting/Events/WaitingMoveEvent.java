package cn.plixe.waitbeforerespawn.waiting.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import cn.plixe.waitbeforerespawn.ConfigFiles;
import cn.plixe.waitbeforerespawn.waiting.WaitingAPI;

public class WaitingMoveEvent implements Listener {

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {

		if (WaitingAPI.playersWaitingList.contains(e.getPlayer())) {

			if (e.getTo().getX() == e.getFrom().getX() && e.getTo().getY() == e.getFrom().getY()
					&& e.getTo().getZ() == e.getFrom().getZ()) {

				return;

			} else {

				if (ConfigFiles.settingsConf.getBoolean("waiting-room.enable")) {

					if (ConfigFiles.settingsConf.getBoolean("waiting-room.player-moves")) {

						if (WaitingAPI.waitingRoomDefined()) {

							e.setCancelled(false);

						} else {

							WaitingAPI.teleportToWaitingRoom(e.getPlayer());

						}

					} else {

						e.getPlayer().teleport(e.getFrom());

					}

				} else {

					e.getPlayer().teleport(e.getFrom());

				}

			}

		}

	}

}
