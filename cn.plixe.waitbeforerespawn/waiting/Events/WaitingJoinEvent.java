package cn.plixe.waitbeforerespawn.waiting.Events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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

				Utils.sendColoredMessage(player, ConfigFiles.msgConf.getString("waiting-messages.reset-on-quit"));

			} else {

				count = ConfigFiles.wSavesConf.getInt("saves." + WaitingAPI.playerPath(player) + ".remaining-time");

			}

			/* WAITER */

			BukkitTask countdownTask = new BukkitRunnable() {

				int counter = count;
				int oneTime = 1;

				@Override
				public void run() {

					if (player.isOnline()) {

						if (oneTime == 1) {
							if (ConfigFiles.settingsConf.getBoolean("spectator-mode.enable")
									&& !Utils.getVersion().contains("1.7")
									&& ConfigFiles.settingsConf.getBoolean("spectator-mode.continue-on-join")) {

								player.teleport(player.getLocation());

							} else if (ConfigFiles.settingsConf.getBoolean("waiting-room.enable")) {

								player.setGameMode(GameMode.valueOf(ConfigFiles.wSavesConf
										.getString("saves." + WaitingAPI.playerPath(player) + ".gamemode")));

								if (WaitingAPI.waitingRoomDefined()) {

									WaitingAPI.teleportToWaitingRoom(player);

								} else {

									Utils.sendColoredMessage(Bukkit.getConsoleSender(),
											"&cYou need to define waiting-room location ! Use /wbr setroom");

								}

							} else {

								player.setGameMode(GameMode.valueOf(ConfigFiles.wSavesConf
										.getString("saves." + WaitingAPI.playerPath(player) + ".gamemode")));

								player.teleport(WaitingAPI.getRespawnLocation(player));

							}

							oneTime--;

						}

						if (counter < 0) {

							if (ConfigFiles.settingsConf.getBoolean("messages-settings.after-message.enable")) {

								WaitingAPI.sendAfterNotifications(player);

							}

							player.teleport(WaitingAPI.getRespawnLocation(player));

							player.setGameMode(GameMode.valueOf(ConfigFiles.wSavesConf
									.getString("saves." + WaitingAPI.playerPath(player) + ".gamemode")));

							WaitingAPI.playersWaitingList.remove(player);

							ConfigFiles.wSavesConf.set("saves." + WaitingAPI.playerPath(player), null);
							ConfigFiles.saveWaitingSavesFile();

							for (PotionEffect effect : player.getActivePotionEffects()) {

								player.removePotionEffect(effect.getType());

							}

							cancel();

						} else {

							WaitingAPI.sendWaitingNotifications(player, counter);

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

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent e) {

		if (ConfigFiles.settingsConf.getBoolean("form-message")) {

			if (e.getPlayer().hasPermission("wbr.reload")) {

				BukkitTask countdownTask = new BukkitRunnable() {

					public void run() {

						Utils.sendColoredMessage(e.getPlayer(), "");
						Utils.sendColoredMessage(e.getPlayer(),
								"&a&l[WaitBeforeRespawn] : &aHelp me to make this plugin better with this form https://goo.gl/forms/ut7kLgNylNb2AjlA2");
						Utils.sendColoredMessage(e.getPlayer(),
								"&eOnly admin can see this message. Disable it on settings.yml file. (see last line)");
						Utils.sendColoredMessage(e.getPlayer(), "&eSorry for the trouble - Plixe.");
						Utils.sendColoredMessage(e.getPlayer(), "");

						cancel();

					}

				}.runTaskTimer(Main.instance, 20 * 10, 20);

			}

		}

	}

}
