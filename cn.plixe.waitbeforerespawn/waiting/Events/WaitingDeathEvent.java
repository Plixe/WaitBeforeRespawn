package cn.plixe.waitbeforerespawn.waiting.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import cn.plixe.waitbeforerespawn.ConfigFiles;
import cn.plixe.waitbeforerespawn.Utils;
import cn.plixe.waitbeforerespawn.waiting.WaitingAPI;

public class WaitingDeathEvent implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDie(PlayerDeathEvent e) {

		Player player = e.getEntity().getPlayer();
		Location loc = player.getLocation();

		ConfigFiles.wSavesConf.set("saves." + WaitingAPI.playerPath(player) + ".gamemode", player.getGameMode().name());

		if (ConfigFiles.settingsConf.getBoolean("spectator-mode.enable") && !Utils.getVersion().contains("1.7")) {

			ConfigFiles.wSavesConf.set("saves." + WaitingAPI.playerPath(player) + ".death-location.world",
					loc.getWorld().getName());
			ConfigFiles.wSavesConf.set("saves." + WaitingAPI.playerPath(player) + ".death-location.x", loc.getX());
			ConfigFiles.wSavesConf.set("saves." + WaitingAPI.playerPath(player) + ".death-location.y", loc.getY());
			ConfigFiles.wSavesConf.set("saves." + WaitingAPI.playerPath(player) + ".death-location.z", loc.getZ());
			ConfigFiles.wSavesConf.set("saves." + WaitingAPI.playerPath(player) + ".death-location.yaw", loc.getYaw());
			ConfigFiles.wSavesConf.set("saves." + WaitingAPI.playerPath(player) + ".death-location.pitch",
					loc.getPitch());
			ConfigFiles.saveWaitingSavesFile();

		}

	}

}
