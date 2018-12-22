package cn.plixe.waitbeforerespawn.waiting;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cn.plixe.waitbeforerespawn.ConfigFiles;
import cn.plixe.waitbeforerespawn.Utils;

public class WaitingCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (label.equalsIgnoreCase("wbr")) {

			if (args.length == 0) {

				if (sender.hasPermission("wbr.help")) {

					List<String> helpMessages = (List<String>) ConfigFiles.msgConf.getList("help-page");

					for (int i = 0; i < helpMessages.size(); i++) {

						Utils.sendColoredMessage(sender, helpMessages.get(i));

					}

				} else {

					Utils.sendNoPermMessage(sender);

				}

			} else if (args.length == 1) {

				if (args[0].equalsIgnoreCase("reload")) {

					if (sender.hasPermission("wbr.reload")) {

						ConfigFiles.reloadFiles(sender);

					} else {

						Utils.sendNoPermMessage(sender);

					}

				} else if (args[0].equalsIgnoreCase("setroom")) {

					if (sender instanceof Player) {

						Player player = ((Player) sender).getPlayer();

						if (player.hasPermission("wbr.setroom")) {

							WaitingAPI.setRoom(player);

						} else {

							Utils.sendNoPermMessage(player);

						}

					} else {

						Utils.sendColoredMessage(sender, "&cYou need to be a player !");

					}

				} else if (args[0].equalsIgnoreCase("tproom")) {

					if (sender instanceof Player) {

						Player player = ((Player) sender).getPlayer();

						if (player.hasPermission("wbr.tproom")) {

							WaitingAPI.teleportToWaitingRoom(player);

						} else {

							Utils.sendNoPermMessage(player);

						}

					} else {

						Utils.sendColoredMessage(sender, "&cYou need to be a player !");

					}

				} else {

					Utils.sendColoredMessage(sender, ConfigFiles.msgConf.getString("global-messages.unknow-command"));

				}

			} else {

				Utils.sendColoredMessage(sender, ConfigFiles.msgConf.getString("global-messages.unknow-command"));

			}

		}

		return false;
	}

}
