package cn.plixe.waitbeforerespawn.waiting.Events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import cn.plixe.waitbeforerespawn.ConfigFiles;
import cn.plixe.waitbeforerespawn.Main;
import cn.plixe.waitbeforerespawn.Utils;
import cn.plixe.waitbeforerespawn.waiting.WaitingAPI;

public class WaitingRespawnEvent implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerRespawn(final PlayerRespawnEvent e) {

		final Player player = e.getPlayer();

		if (player.hasPermission("wbr.bypass")) {

			return;

		} else {

			final int waitingTime = ConfigFiles.settingsConf.getInt("global-settings.waiting-time");

			WaitingAPI.playersWaitingList.add(player);

			/* WAITER */

			BukkitTask countdownTask = new BukkitRunnable() {

				int counter = waitingTime;
				int oneTime = 1;

				@Override
				public void run() {

					if (player.isOnline()) {

						if (oneTime == 1) {

							if (ConfigFiles.settingsConf.getBoolean("spectator-mode.enable")
									&& !Utils.getVersion().contains("1.7")) {

								player.teleport(WaitingAPI.getDeathLocation(player));
								player.setGameMode(GameMode.valueOf("SPECTATOR"));

							} else if (ConfigFiles.settingsConf.getBoolean("waiting-room.enable")) {

								if (WaitingAPI.waitingRoomDefined()) {

									WaitingAPI.teleportToWaitingRoom(player);

								} else {

									Utils.sendColoredMessage(Bukkit.getConsoleSender(),
											"&cYou need to define waiting-room location ! Use /wbr setroom");

								}

							} else {

								player.teleport(e.getRespawnLocation());

							}

							oneTime--;

						}

						if (counter < 0) {

							if (ConfigFiles.settingsConf.getBoolean("messages-settings.after-message.enable")) {

								WaitingAPI.sendAfterNotifications(player);

							}

							player.teleport(e.getRespawnLocation());

							WaitingAPI.playersWaitingList.remove(player);

							player.setGameMode(GameMode.valueOf(ConfigFiles.wSavesConf
									.getString("saves." + WaitingAPI.playerPath(player) + ".gamemode")));

							ConfigFiles.wSavesConf.set("saves." + WaitingAPI.playerPath(player), null);
							ConfigFiles.saveWaitingSavesFile();

							for (PotionEffect effect : player.getActivePotionEffects()) {

								player.removePotionEffect(effect.getType());

							}

							cancel();

						} else {

							WaitingAPI.sendWaitingNotifications(player, counter);

							counter--;

						}

					} else {

						ConfigFiles.wSavesConf.set("saves." + WaitingAPI.playerPath(player) + ".respawn-location.world",
								e.getRespawnLocation().getWorld().getName());
						ConfigFiles.wSavesConf.set("saves." + WaitingAPI.playerPath(player) + ".respawn-location.x",
								e.getRespawnLocation().getX());
						ConfigFiles.wSavesConf.set("saves." + WaitingAPI.playerPath(player) + ".respawn-location.y",
								e.getRespawnLocation().getY());
						ConfigFiles.wSavesConf.set("saves." + WaitingAPI.playerPath(player) + ".respawn-location.z",
								e.getRespawnLocation().getZ());
						ConfigFiles.wSavesConf.set("saves." + WaitingAPI.playerPath(player) + ".respawn-location.yaw",
								e.getRespawnLocation().getYaw());
						ConfigFiles.wSavesConf.set("saves." + WaitingAPI.playerPath(player) + ".respawn-location.pitch",
								e.getRespawnLocation().getPitch());
						ConfigFiles.wSavesConf.set("saves." + WaitingAPI.playerPath(player) + ".remaining-time",
								counter);
						ConfigFiles.saveWaitingSavesFile();

						player.setGameMode(GameMode.valueOf("SURVIVAL"));

						cancel();

					}

				}

			}.runTaskTimer(Main.instance, 20, 20);

		}

	}

}
