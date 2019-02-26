package cn.plixe.waitbeforerespawn.waiting.Events;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
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

	static void sendAfterNotifications(Player player) {

		if (ConfigFiles.settingsConf.getBoolean("messages-settings.after-message.chat-message.enable")) {

			if (ConfigFiles.settingsConf.getBoolean("messages-settings.after-message.chat-message.space")) {

				player.sendMessage("");
				Utils.sendColoredMessage(player, ConfigFiles.msgConf.getString("waiting-messages.after"));
				player.sendMessage("");

			} else {

				Utils.sendColoredMessage(player, ConfigFiles.msgConf.getString("waiting-messages.after"));

			}

		}

		if (ConfigFiles.settingsConf.getBoolean("messages-settings.after-message.bar-message")) {

			Utils.sendActionBar(player, ConfigFiles.msgConf.getString("waiting-messages.after"));

		}

		if (ConfigFiles.settingsConf.getBoolean("messages-settings.after-message.title-message")) {

			Utils.sendTitle(player, "", ConfigFiles.msgConf.getString("waiting-messages.after"));

		}

	}

	static void sendWaitingNotifications(Player player, int counter) {

		if (ConfigFiles.settingsConf.getBoolean("messages-settings.chat-message.enable")) {

			if (ConfigFiles.settingsConf.getBoolean("messages-settings.chat-message.space")) {

				player.sendMessage("");
				Utils.sendColoredMessage(player,
						ConfigFiles.msgConf.getString("waiting-messages.countdown").replace("<seconds>", "" + counter));
				player.sendMessage("");

			} else {

				Utils.sendColoredMessage(player,
						ConfigFiles.msgConf.getString("waiting-messages.countdown").replace("<seconds>", "" + counter));

			}

		}

		if (ConfigFiles.settingsConf.getBoolean("messages-settings.bar-message")) {

			Utils.sendActionBar(player,
					ConfigFiles.msgConf.getString("waiting-messages.countdown").replace("<seconds>", "" + counter));

		}

		if (ConfigFiles.settingsConf.getBoolean("messages-settings.title-message")) {

			Utils.sendTitle(player,
					ConfigFiles.msgConf.getString("waiting-messages.title.top").replace("<seconds>", "" + counter),
					ConfigFiles.msgConf.getString("waiting-messages.title.subtitle").replace("<seconds>",
							"" + counter));

		}

		if (ConfigFiles.settingsConf.getBoolean("sounds-settings.enable")) {

			player.playSound(player.getLocation(),
					Sound.valueOf(ConfigFiles.settingsConf.getString("sounds-settings.name")), 1, 1);

		}

	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerRespawn(final PlayerRespawnEvent e) {

		final Player player = e.getPlayer();

		if (player.hasPermission("wbr.bypass")) {

			return;

		} else {

			final int waitingTime = ConfigFiles.settingsConf.getInt("global-settings.waiting-time");

			WaitingAPI.playersWaitingList.add(player);

			if (ConfigFiles.settingsConf.getBoolean("waiting-room.enable") && (!WaitingAPI.waitingRoomDefined())) {

				Utils.sendColoredMessage(Bukkit.getConsoleSender(),
						"&cYou need to define waiting-room location ! Use /wbr setroom");

			}

			/* WAITER */

			BukkitTask countdownTask = new BukkitRunnable() {

				int counter = waitingTime;
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

							}

						}

						if (counter < 0) {

							if (ConfigFiles.settingsConf.getBoolean("messages-settings.after-message.enable")) {

								sendAfterNotifications(player);

							}

							player.teleport(e.getRespawnLocation());

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

							sendWaitingNotifications(player, counter);

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

						cancel();

					}

				}

			}.runTaskTimer(Main.instance, 20, 20);

		}

	}

}
