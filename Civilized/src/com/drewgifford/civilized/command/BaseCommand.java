package com.drewgifford.civilized.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.command.subcommands.HelpCommand;

import net.md_5.bungee.api.ChatColor;

public class BaseCommand implements CommandExecutor {
	
	private Civilized pl;
	private CivilizedSubcommand defaultCommand;
	private List<CivilizedSubcommand> subcommands;
	
	public BaseCommand(Civilized pl, List<CivilizedSubcommand> subcommands, CivilizedSubcommand defaultCommand) {
		this.pl = pl;
		this.subcommands = subcommands;
		this.defaultCommand = defaultCommand;
	
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Civilized command
		
		if(args.length == 0) {
			return defaultCommand.run(sender, new String[] {});
		}
		
		String sub = args[0];
		String[] subcommandArgs = new String[] {};
		
		if (args.length > 1) {
			subcommandArgs = Arrays.copyOfRange(args, 1, args.length);
		}
		
		for (CivilizedSubcommand subcommand : this.subcommands) {
			System.out.println(sub + ": " + subcommand.getLabel().toString() + " " + subcommand.getAliases());
			if (subcommand.hasLabel(sub)) {
				return subcommand.run(sender, subcommandArgs);
			}
		}
		
		// Command not found
		sender.sendMessage(ChatColor.RED + "Command not found.");
		
		
		
		
		return false;
	}
	
	
	
}