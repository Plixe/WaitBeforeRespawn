package cn.plixe.waitbeforerespawn.waiting.Events;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import cn.plixe.waitbeforerespawn.ConfigFiles;
import cn.plixe.waitbeforerespawn.Main;
import cn.plixe.waitbeforerespawn.Utils;
import cn.plixe.waitbeforerespawn.waiting.WaitingAPI;

public class WaitingRespawnEvent implements Listener {

	private void sendAfterNotifications(Player player) {

		if (ConfigFiles.settingsConf.getBoolean("messages-settings.after-messages.chat-message.enable")) {

			if (ConfigFiles.settingsConf.getBoolean("messages-settings.after-messages.chat-message.space")) {

				player.sendMessage("");
				Utils.sendColoredMessage(player, ConfigFiles.msgConf.getString("waiting-messages.after"));
				player.sendMessage("");

			} else {

				Utils.sendColoredMessage(player, ConfigFiles.msgConf.getString("waiting-messages.after"));

			}

		}

		if (ConfigFiles.settingsConf.getBoolean("messages-settings.after-messages.bar-message")) {

			Utils.sendActionBar(player, ConfigFiles.msgConf.getString("waiting-messages.after"));

		}

		if (ConfigFiles.settingsConf.getBoolean("messages-settings.after-messages.title-message")) {

			Utils.sendTitle(player, "", ConfigFiles.msgConf.getString("waiting-messages.after"));

		}

	}

	private void sendWaitingNotifications(Player player, int counter) {

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

			/* WAITER */

			BukkitTask countdownTask = new BukkitRunnable() {

				int counter = waitingTime;
				int oneTime = 1;

				@Override
				public void run() {

					if (ConfigFiles.settingsConf.getBoolean("global-settings.waiting-room")) {

						if (oneTime == 1) {

							WaitingAPI.teleportToWaitingRoom(player);
							oneTime--;

						}

					}

					if (counter < 0) {

						if (ConfigFiles.settingsConf.getBoolean("messages-settings.after-messages.enable")) {

							sendAfterNotifications(player);

						}

						player.teleport(e.getRespawnLocation());
						cancel();

					} else {

						sendWaitingNotifications(player, counter);

						counter--;

					}

				}

			}.runTaskTimer(Main.instance, 20, 20);

			/* WAITER REMOVER */

			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {

				public void run() {

					WaitingAPI.playersWaitingList.remove(player);

					for (PotionEffect effect : player.getActivePotionEffects()) {

						player.removePotionEffect(effect.getType());

					}

				}

			}, (waitingTime + 1) * 20);

		}

	}

}
