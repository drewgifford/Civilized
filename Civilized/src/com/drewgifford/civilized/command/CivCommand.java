package com.drewgifford.civilized.command;

import java.util.ArrayList;
import java.util.List;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.command.subcommands.*;
import com.drewgifford.civilized.command.subcommands.city.CityDenyCommand;
import com.drewgifford.civilized.command.subcommands.civ.CivMapCommand;
import com.drewgifford.civilized.command.subcommands.civ.CivReloadCommand;

public class CivCommand {
	
	public static void registerCommands(Civilized pl) {
		
		List<CivilizedSubcommand> subcommands = new ArrayList<CivilizedSubcommand>();
		
		HelpCommand helpCommand = new HelpCommand(pl, "help", new String[] {"?", "helpme"}, "", "Shows the help menu.", subcommands, "Civilized", "civ");
		
		subcommands.add(
				helpCommand
		);
		
		subcommands.add(
				new CivMapCommand(pl, "map", new String[] {}, "", "Shows a map of surrounding cities.")
		);
		
		subcommands.add(
				new CivReloadCommand(pl, "reload", new String[] {}, "", "Reloads configuration files.")
		);
		
		pl.getCommand("civ").setExecutor(new BaseCommand(pl, subcommands, helpCommand));
		
	}
}


