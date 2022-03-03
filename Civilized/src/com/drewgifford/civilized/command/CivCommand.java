package com.drewgifford.civilized.command;

import java.util.ArrayList;
import java.util.List;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.command.subcommands.*;
import com.drewgifford.civilized.command.subcommands.city.CityDenyCommand;
import com.drewgifford.civilized.command.subcommands.civ.CivBypassCommand;
import com.drewgifford.civilized.command.subcommands.civ.CivMapCommand;
import com.drewgifford.civilized.command.subcommands.civ.CivReloadCommand;
import com.drewgifford.civilized.command.subcommands.civ.CivTrustCommand;
import com.drewgifford.civilized.command.subcommands.civ.CivTrustedCommand;
import com.drewgifford.civilized.command.subcommands.civ.CivUntrustCommand;

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
		
		subcommands.add(
				new CivTrustCommand(pl, "trust", new String[] {"friend", "addfriend", "addtrust"}, "", "Allows a player to build in your claimed plots.")
		);
		
		subcommands.add(
				new CivUntrustCommand(pl, "untrust", new String[] {"unfriend", "removefriend", "removetrust", "revoke"}, "", "Revokes a player's ability to build in your claimed plots.")
		);
		
		subcommands.add(
				new CivTrustedCommand(pl, "trusted", new String[] {"friends", "trustlist", "friendlist"}, "", "Lists all of your trusted players.")
		);
		
		subcommands.add(
				new CivBypassCommand(pl, "bypass", new String[] {"override"}, "", "Bypasses claim permissions.")
		);
		
		pl.getCommand("civ").setExecutor(new BaseCommand(pl, subcommands, helpCommand));
		
	}
}


