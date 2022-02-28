package com.drewgifford.civilized.command;

import java.util.ArrayList;
import java.util.List;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.command.subcommands.HelpCommand;
import com.drewgifford.civilized.command.subcommands.city.CityClaimCommand;
import com.drewgifford.civilized.command.subcommands.city.CityDenyCommand;
import com.drewgifford.civilized.command.subcommands.city.CityInfoCommand;
import com.drewgifford.civilized.command.subcommands.city.CityInviteCommand;
import com.drewgifford.civilized.command.subcommands.city.CityJoinCommand;
import com.drewgifford.civilized.command.subcommands.city.CityShopCommand;
import com.drewgifford.civilized.command.subcommands.city.CityUnclaimCommand;
import com.drewgifford.civilized.command.subcommands.city.CreateCityCommand;

public class CityCommand {

public static void registerCommands(Civilized pl) {
		
		List<CivilizedSubcommand> subcommands = new ArrayList<CivilizedSubcommand>();
		
		HelpCommand helpCommand = new HelpCommand(pl, "help", new String[] {"?", "helpme"}, "", "Shows the help menu.", subcommands, "City", "city");
		
		CityInfoCommand cityInfoCommand = new CityInfoCommand(pl, "info", new String[] {"check"}, "", "Shows information about your city or another city.");
		
		subcommands.add(helpCommand);
		subcommands.add(cityInfoCommand);
		
		subcommands.add(
				new CreateCityCommand(pl, "create", new String[] {"found", "new"}, "", "Creates a new city.")
		);
		subcommands.add(
				new CityInviteCommand(pl, "invite", new String[] {"add"}, "", "Invites a player to your city.")
		);
		
		subcommands.add(
				new CityJoinCommand(pl, "join", new String[] {"accept", "yes"}, "", "Accepts an invite to join a city.")
		);
		
		subcommands.add(
				new CityDenyCommand(pl, "deny", new String[] {"no"}, "", "Denies an invite to join a city.")
		);
		
		subcommands.add(
				new CityClaimCommand(pl, "claim", new String[] {}, "", "Claims a chunk for your city.")
		);
		
		subcommands.add(
				new CityUnclaimCommand(pl, "unclaim", new String[] {}, "", "Removes a claim chunk from your city.")
		);
		
		subcommands.add(
				new CityShopCommand(pl, "shop", new String[] {"store", "market", "upgrades", "boosts", "menu", "gui"}, "", "Opens your city's upgrades shop.")
		);
		
		pl.getCommand("city").setExecutor(new BaseCommand(pl, subcommands, cityInfoCommand));
		
	}
	
}
