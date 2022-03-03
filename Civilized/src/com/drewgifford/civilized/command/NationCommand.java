package com.drewgifford.civilized.command;

import java.util.ArrayList;
import java.util.List;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.command.subcommands.HelpCommand;
import com.drewgifford.civilized.command.subcommands.city.CityBoardCommand;
import com.drewgifford.civilized.command.subcommands.city.CityClaimCommand;
import com.drewgifford.civilized.command.subcommands.city.CityDenyCommand;
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
import com.drewgifford.civilized.command.subcommands.city.CityShopCommand;
import com.drewgifford.civilized.command.subcommands.city.CityTogglesCommand;
import com.drewgifford.civilized.command.subcommands.city.CityUnclaimCommand;
import com.drewgifford.civilized.command.subcommands.civ.CivReloadCommand;
import com.drewgifford.civilized.command.subcommands.nation.NationBoardCommand;
import com.drewgifford.civilized.command.subcommands.nation.NationCapitalCommand;
import com.drewgifford.civilized.command.subcommands.nation.NationCreateCommand;
import com.drewgifford.civilized.command.subcommands.nation.NationDemoteCommand;
import com.drewgifford.civilized.command.subcommands.nation.NationDenyCommand;
import com.drewgifford.civilized.command.subcommands.nation.NationDisbandCommand;
import com.drewgifford.civilized.command.subcommands.nation.NationHomeCommand;
import com.drewgifford.civilized.command.subcommands.nation.NationInfoCommand;
import com.drewgifford.civilized.command.subcommands.nation.NationInviteCommand;
import com.drewgifford.civilized.command.subcommands.nation.NationJoinCommand;
import com.drewgifford.civilized.command.subcommands.nation.NationKickCommand;
import com.drewgifford.civilized.command.subcommands.nation.NationLeaveCommand;
import com.drewgifford.civilized.command.subcommands.nation.NationNameCommand;
import com.drewgifford.civilized.command.subcommands.nation.NationPromoteCommand;
import com.drewgifford.civilized.command.subcommands.plot.PlotClaimCommand;
import com.drewgifford.civilized.command.subcommands.plot.PlotForSaleCommand;
import com.drewgifford.civilized.command.subcommands.plot.PlotInfoCommand;
import com.drewgifford.civilized.command.subcommands.plot.PlotNotForSaleCommand;
import com.drewgifford.civilized.command.subcommands.plot.PlotPermissionsCommand;
import com.drewgifford.civilized.command.subcommands.plot.PlotTogglesCommand;
import com.drewgifford.civilized.command.subcommands.plot.PlotUnclaimCommand;
import com.drewgifford.civilized.command.subcommands.city.CityCreateCommand;
import com.drewgifford.civilized.command.subcommands.city.CityDemoteCommand;

public class NationCommand {

public static void registerCommands(Civilized pl) {
		
		List<CivilizedSubcommand> subcommands = new ArrayList<CivilizedSubcommand>();
		
		HelpCommand helpCommand = new HelpCommand(pl, "help", new String[] {"?", "helpme"}, "", "Shows the help menu.", subcommands, "Nation", "nation");
		
		NationInfoCommand nationInfoCommand = new NationInfoCommand(pl, "info", new String[] {"check"}, "", "Shows information about your nation or another nation.");
		
		subcommands.add(helpCommand);
		subcommands.add(nationInfoCommand);
		
		subcommands.add(
				new NationCreateCommand(pl, "create", new String[] {"found", "new"}, "", "Creates a new nation. You must own a city to use this command.")
		);
		
		subcommands.add(
				new NationLeaveCommand(pl, "leave", new String[] {"exit", "quit"}, "", "Leaves a nation.")
		);
		
		subcommands.add(
				new NationBoardCommand(pl, "board", new String[] {"description", "desc", "motd", "message", "setboard"}, "", "Sets your nation's board message.")
		);
		
		subcommands.add(
				new NationInviteCommand(pl, "invite", new String[] {"add"}, "", "Invites a city to your nation.")
		);
		
		subcommands.add(
				new NationJoinCommand(pl, "join", new String[] {"accept", "yes"}, "", "Accepts an invite to join a nation.")
		);
		
		subcommands.add(
				new NationDenyCommand(pl, "deny", new String[] {"no"}, "", "Denies an invite to join a nation.")
		);
		
		
		subcommands.add(
				new NationPromoteCommand(pl, "promote", new String[] {"officer", "rankup"}, "", "Promotes a nation member to Officer.")
		);
		
		subcommands.add(
				new NationDemoteCommand(pl, "demote", new String[] {"derank", "rankdown"}, "", "Demotes a nation Officer.")
		);
		
		subcommands.add(
				new NationDisbandCommand(pl, "disband", new String[] {"delete"}, "", "Disbands your nation.")
		);
		
		subcommands.add(
				new NationNameCommand(pl, "name", new String[] {"rename"}, "", "Sets your nation's name.")
		);
		
		subcommands.add(
				new NationCapitalCommand(pl, "capital", new String[] {"setcapital", "capitol", "maincity"}, "", "Sets your nation's capital.")
		);
		
		subcommands.add(
				new NationHomeCommand(pl, "home", new String[] {"spawn"}, "", "Teleports to your capital city's home location.")
		);
		
		subcommands.add(
				new NationKickCommand(pl, "kick", new String[] {"ban"}, "", "Kicks a city from your nation.")
		);
		
		pl.getCommand("nation").setExecutor(new BaseCommand(pl, subcommands, nationInfoCommand));
		
	}
	
}
