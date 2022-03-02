package com.drewgifford.civilized.command;

import java.util.ArrayList;
import java.util.List;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.command.subcommands.HelpCommand;
import com.drewgifford.civilized.command.subcommands.city.CityBoardCommand;
import com.drewgifford.civilized.command.subcommands.city.CityClaimCommand;
import com.drewgifford.civilized.command.subcommands.city.CityDenyCommand;
import com.drewgifford.civilized.command.subcommands.city.CityInfoCommand;
import com.drewgifford.civilized.command.subcommands.city.CityInviteCommand;
import com.drewgifford.civilized.command.subcommands.city.CityJoinCommand;
import com.drewgifford.civilized.command.subcommands.city.CityLeaveCommand;
import com.drewgifford.civilized.command.subcommands.city.CityNameCommand;
import com.drewgifford.civilized.command.subcommands.city.CityPermissionsCommand;
import com.drewgifford.civilized.command.subcommands.city.CityPromoteCommand;
import com.drewgifford.civilized.command.subcommands.city.CityShopCommand;
import com.drewgifford.civilized.command.subcommands.city.CityTogglesCommand;
import com.drewgifford.civilized.command.subcommands.city.CityUnclaimCommand;
import com.drewgifford.civilized.command.subcommands.plot.PlotClaimCommand;
import com.drewgifford.civilized.command.subcommands.plot.PlotForSaleCommand;
import com.drewgifford.civilized.command.subcommands.plot.PlotInfoCommand;
import com.drewgifford.civilized.command.subcommands.plot.PlotNotForSaleCommand;
import com.drewgifford.civilized.command.subcommands.plot.PlotPermissionsCommand;
import com.drewgifford.civilized.command.subcommands.plot.PlotTogglesCommand;
import com.drewgifford.civilized.command.subcommands.plot.PlotUnclaimCommand;
import com.drewgifford.civilized.command.subcommands.city.CityCreateCommand;
import com.drewgifford.civilized.command.subcommands.city.CityDemoteCommand;

public class PlotCommand {

public static void registerCommands(Civilized pl) {
		
		List<CivilizedSubcommand> subcommands = new ArrayList<CivilizedSubcommand>();
		
		HelpCommand helpCommand = new HelpCommand(pl, "help", new String[] {"?", "helpme"}, "", "Shows the help menu.", subcommands, "Plot", "plot");
		
		PlotInfoCommand plotInfoCommand = new PlotInfoCommand(pl, "info", new String[] {"check"}, "", "Shows information about the plot you are standing on.");
		
		subcommands.add(helpCommand);
		subcommands.add(plotInfoCommand);
		
		subcommands.add(
				new PlotForSaleCommand(pl, "forsale", new String[] {"list", "sell"}, "", "Lists a plot for sale.")
		);
		
		subcommands.add(
				new PlotNotForSaleCommand(pl, "notforsale", new String[] {"unlist"}, "", "Unlists a plot.")
		);
		
		subcommands.add(
				new PlotClaimCommand(pl, "claim", new String[] {}, "", "Claims a plot that is for sale.")
		);
		
		subcommands.add(
				new PlotUnclaimCommand(pl, "unclaim", new String[] {}, "", "Unclaims a plot that you own.")
		);
		
		subcommands.add(
				new PlotPermissionsCommand(pl, "permission", new String[] {"perm"}, "", "Manages plot permissions.")
		);
		
		subcommands.add(
				new PlotTogglesCommand(pl, "toggle", new String[] {"switch", "options", "set"}, "", "Manages plot toggles.")
		);
		
		pl.getCommand("plot").setExecutor(new BaseCommand(pl, subcommands, plotInfoCommand));
		
	}
	
}
