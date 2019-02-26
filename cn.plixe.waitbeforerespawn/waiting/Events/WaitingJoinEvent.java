package cn.plixe.waitbeforerespawn.waiting.Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import cn.plixe.waitbeforerespawn.ConfigFiles;
import cn.plixe.waitbeforerespawn.Main;
import cn.plixe.waitbeforerespawn.Utils;
import cn.plixe.waitbeforerespawn.waiting.WaitingAPI;

public class WaitingJoinEvent implements Listener {

	static Location getRespawnLocation(Player player) {

		World world = Bukkit.getServer().getWorld(
				ConfigFiles.wSavesConf.getString("saves." + WaitingAPI.playerPath(player) + ".respawn-location.world"));
		Double x = ConfigFiles.wSavesConf.getDouble("saves." + WaitingAPI.playerPath(player) + ".respawn-location.x");
		Double y = ConfigFiles.wSavesConf.getDouble("saves." + WaitingAPI.playerPath(player) + ".respawn-location.y");
		Double z = ConfigFiles.wSavesConf.getDouble("saves." + WaitingAPI.playerPath(player) + ".respawn-location.z");
		Float yaw = (float) ConfigFiles.wSavesConf
				.getInt("saves." + WaitingAPI.playerPath(player) + ".respawn-location.yaw");
		Float pitch = (float) ConfigFiles.wSavesConf
				.getInt("saves." + WaitingAPI.playerPath(player) + ".respawn-location.pitch");

		Location location = new Location(world, x, y, z, yaw, pitch);

		return location;

	}

	@EventHandler
	public void onWaitingJoin(PlayerJoinEvent e) {

		final Player player = e.getPlayer();
		int waitingTime = ConfigFiles.settingsConf.getInt("global-settings.waiting-time");

		if (ConfigFiles.wSavesConf.contains("saves." + WaitingAPI.playerPath(player))) {

			WaitingAPI.playersWaitingList.add(player);

			/*
			 * WAITER - I know... it's just a copy/paste of WaitingRespawnEvent, I'M A
			 * BEGINNER OK ?!
			 */

			final int count;

			if (ConfigFiles.settingsConf.getBoolean("anti-unlogin.reset-on-quit")) {

				count = ConfigFiles.settingsConf.getInt("global-settings.waiting-time");

			} else {

				count = ConfigFiles.wSavesConf.getInt("saves." + WaitingAPI.playerPath(player) + ".remaining-time");

			}

			BukkitTask countdownTask = new BukkitRunnable() {

				int counter = count;
				int oneTime = 1;

				@Override
				public void run() {

					if (player.isOnline()) {

						if (ConfigFiles.settingsConf.getBoolean("waiting-room.enable")) {

							if (WaitingAPI.waitingRoomDefined()) {

								if (oneTime == 1) {

									WaitingAPI.teleportToWaitingRoom(player);
									oneTime--;

								}

							} else {

								Utils.sendColoredMessage(Bukkit.getConsoleSender(),
										ConfigFiles.msgConf.getString("waiting-room-messages.undefined-room"));

							}

						}

						if (counter < 0) {

							if (ConfigFiles.settingsConf.getBoolean("messages-settings.after-message.enable")) {

								WaitingRespawnEvent.sendAfterNotifications(player);

							}

							player.teleport(getRespawnLocation(player));

							if (player.isOnline()) {

								WaitingAPI.playersWaitingList.remove(player);

								ConfigFiles.wSavesConf.set("saves." + WaitingAPI.playerPath(player), null);
								ConfigFiles.saveWaitingSavesFile();

								for (PotionEffect effect : player.getActivePotionEffects()) {

									player.removePotionEffect(effect.getType());

								}

							}

							cancel();

						} else {

							WaitingRespawnEvent.sendWaitingNotifications(player, counter);

							ConfigFiles.wSavesConf.set("saves." + WaitingAPI.playerPath(player) + ".remaining-time",
									counter);
							ConfigFiles.saveWaitingSavesFile();

							counter--;

						}

					} else {

						ConfigFiles.wSavesConf.set("saves." + WaitingAPI.playerPath(player) + ".remaining-time",
								counter);
						ConfigFiles.saveWaitingSavesFile();

						cancel();

					}

				}

			}.runTaskTimer(Main.instance, 20, 20);

		}

	}

}
