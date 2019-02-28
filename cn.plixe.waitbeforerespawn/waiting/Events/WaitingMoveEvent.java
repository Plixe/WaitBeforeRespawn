package cn.plixe.waitbeforerespawn.waiting.Events;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import cn.plixe.waitbeforerespawn.ConfigFiles;
import cn.plixe.waitbeforerespawn.Utils;
import cn.plixe.waitbeforerespawn.waiting.WaitingAPI;

public class WaitingMoveEvent implements Listener {

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {

		Player player = e.getPlayer();

		if (WaitingAPI.playersWaitingList.contains(e.getPlayer())) {

			if (e.getTo().getX() == e.getFrom().getX() && e.getTo().getY() == e.getFrom().getY()
					&& e.getTo().getZ() == e.getFrom().getZ()) {

				return;

			} else {

				if (ConfigFiles.settingsConf.getBoolean("spectator-mode.enable")
						&& !Utils.getVersion().contains("1.7")) {

					if (player.getGameMode() == GameMode.valueOf("SPECTATOR")) {

						if (ConfigFiles.settingsConf.getBoolean("spectator-mode.through-blocks")) {

							return;

						} else {

							if (e.getTo().getBlock().getType() == Material.AIR) {

								return;

							} else {

								e.setCancelled(true);

							}

						}

					} else {

						player.teleport(e.getFrom());

					}

				} else if (ConfigFiles.settingsConf.getBoolean("waiting-room.enable")) {

					if (ConfigFiles.settingsConf.getBoolean("waiting-room.player-moves")) {

						if (WaitingAPI.waitingRoomDefined()) {

							e.setCancelled(false);

						} else {

							WaitingAPI.teleportToWaitingRoom(e.getPlayer());

						}

					} else {

						player.teleport(e.getFrom());

					}

				} else {

					player.teleport(e.getFrom());

				}

			}

		}

	}

}
