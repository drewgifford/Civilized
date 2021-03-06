package com.drewgifford.civilized.command;

import java.util.ArrayList;
import java.util.List;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.command.subcommands.HelpCommand;
import com.drewgifford.civilized.command.subcommands.city.CityBoardCommand;
import com.drewgifford.civilized.command.subcommands.city.CityClaimCommand;
import com.drewgifford.civilized.command.subcommands.city.CityDenyCommand;
import com.drewgifford.civilized.command.subcommands.city.CityDepositCommand;
import com.drewgifford.civilized.command.subcommands.city.CityDisbandCommand;
import com.drewgifford.civilized.command.subcommands.city.CityHomeCommand;
import com.drewgifford.civilized.command.subcommands.city.CityInfoCommand;
import com.drewgifford.civilized.command.subcommands.city.CityInviteCommand;
import com.drewgifford.civilized.command.subcommands.city.CityJoinCommand;
import com.drewgifford.civilized.command.subcommands.city.CityKickCommand;
import com.drewgifford.civilized.command.subcommands.city.CityLeaveCommand;
import com.drewgifford.civilized.command.subcommands.city.CityNameCommand;
import com.drewgifford.civilized.command.subcommands.city.CityPermissionsCommand;
import com.drewgifford.civilized.command.subcommands.city.CityPromoteCommand;
import com.drewgifford.civilized.command.subcommands.city.CitySethomeCommand;
import com.drewgifford.civilized.command.subcommands.city.CityShopCommand;
import com.drewgifford.civilized.command.subcommands.city.CityTogglesCommand;
import com.drewgifford.civilized.command.subcommands.city.CityUnclaimCommand;
import com.drewgifford.civilized.command.subcommands.city.CityWithdrawCommand;
import com.drewgifford.civilized.command.subcommands.city.CityCreateCommand;
import com.drewgifford.civilized.command.subcommands.city.CityDemoteCommand;

public class CityCommand {

public static void registerCommands(Civilized pl) {
		
		List<CivilizedSubcommand> subcommands = new ArrayList<CivilizedSubcommand>();
		
		HelpCommand helpCommand = new HelpCommand(pl, "help", new String[] {"?", "helpme"}, "", "Shows the help menu.", subcommands, "City", "city");
		
		CityInfoCommand cityInfoCommand = new CityInfoCommand(pl, "info", new String[] {"check"}, "", "Shows information about your city or another city.");
		
		subcommands.add(helpCommand);
		subcommands.add(cityInfoCommand);
		
		subcommands.add(
				new CityCreateCommand(pl, "create", new String[] {"found", "new"}, "", "Creates a new city.")
		);
		subcommands.add(
				new CityInviteCommand(pl, "invite", new String[] {"add"}, "", "Invites a player to your city.")
		);
		
		subcommands.add(
				new CityLeaveCommand(pl, "leave", new String[] {"exit", "quit"}, "", "Leaves a city.")
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
		subcommands.add(
				new CityBoardCommand(pl, "board", new String[] {"description", "desc", "motd", "message", "setboard"}, "", "Sets your city's board message.")
		);
		subcommands.add(
				new CityNameCommand(pl, "name", new String[] {"rename"}, "", "Sets your city's name.")
		);
		
		subcommands.add(
				new CityPromoteCommand(pl, "promote", new String[] {"officer", "rankup"}, "", "Promotes a city member to Officer.")
		);
		
		subcommands.add(
				new CityDemoteCommand(pl, "demote", new String[] {"derank", "rankdown"}, "", "Demotes a city Officer.")
		);
		
		subcommands.add(
				new CityPermissionsCommand(pl, "permission", new String[] {"perm"}, "", "Manages city permissions.")
		);
		
		subcommands.add(
				new CityTogglesCommand(pl, "toggle", new String[] {"switch", "options", "set"}, "", "Manages city toggles.")
		);
		
		subcommands.add(
				new CityDisbandCommand(pl, "disband", new String[] {"delete"}, "", "Disbands your city.")
		);
		
		subcommands.add(
				new CitySethomeCommand(pl, "sethome", new String[] {"setspawn"}, "", "Sets your city's home location.")
		);
		
		subcommands.add(
				new CityHomeCommand(pl, "home", new String[] {"spawn"}, "", "Teleports to your city's home location.")
		);
		
		subcommands.add(
				new CityDepositCommand(pl, "deposit", new String[] {"pay", "donate", "givemoney"}, "", "Deposits money into your city bank.")
		);
		
		subcommands.add(
				new CityWithdrawCommand(pl, "withdraw", new String[] {"takemoney"}, "", "Withdraws money from your city bank.")
		);
		
		subcommands.add(
				new CityKickCommand(pl, "kick", new String[] {"ban"}, "", "Kicks a user from your city.")
		);
		
		pl.getCommand("city").setExecutor(new BaseCommand(pl, subcommands, cityInfoCommand));
		
	}
	
}
