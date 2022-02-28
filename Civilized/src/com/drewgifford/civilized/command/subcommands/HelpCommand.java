package com.drewgifford.civilized.command.subcommands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.command.CivilizedSubcommand;

import net.md_5.bungee.api.ChatColor;

public class HelpCommand extends CivilizedSubcommand{
	
	private List<CivilizedSubcommand> subcommands;
	private String helpLabel;
	private String command;

	public HelpCommand(Civilized pl, String label, String[] aliases, String permission, String description, List<CivilizedSubcommand> subcommands, String helpLabel, String command) {
		super(pl, label, aliases, permission, description);
		this.subcommands = subcommands;
		this.helpLabel = helpLabel;
		this.command = command;
	}

	@Override
	public boolean run(CommandSender sender, String[] args) {
		
		// TODO: Add lang file
		sender.sendMessage("");
		sender.sendMessage(ChatColor.GREEN + "===== " + helpLabel + " Help Menu =====");
		
		for (CivilizedSubcommand subcommand : subcommands) {
			
			if (sender instanceof Player) {
				Player p = (Player) sender;
				
				if (!subcommand.canUse(p)) continue;
			}
			sender.sendMessage(ChatColor.AQUA + "/" + command + " " + subcommand.getLabel() + ChatColor.DARK_AQUA + " - " + ChatColor.GRAY + ChatColor.ITALIC + subcommand.getDescription());

		}
		sender.sendMessage("");
		
		return false;
	}
	
	

}
