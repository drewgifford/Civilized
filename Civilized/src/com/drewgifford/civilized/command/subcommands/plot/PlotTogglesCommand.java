package com.drewgifford.civilized.command.subcommands.plot;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.permissions.CivilizedPermissions;
import com.drewgifford.civilized.permissions.CivilizedToggles;
import com.drewgifford.civilized.permissions.PermissionLevel;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.plot.Plot;
import com.drewgifford.civilized.util.CityManager;

import net.md_5.bungee.api.ChatColor;

public class PlotTogglesCommand extends CivilizedSubcommand {
	
	public PlotTogglesCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
		super(pl, label, aliases, permission, description);
	}

	@Override
	public boolean run(CommandSender sender, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can run this command.");
			return false;
		}
		Player p = (Player) sender;
		CivilizedPlayer cp = CivilizedPlayer.getCivilizedPlayer(p);

		Location location = p.getLocation();
		
		City city = CityManager.getCityFromLocation(location);
	
		if (city == null) {
			p.sendMessage(ChatColor.RED + "There is no plot at this location.");
			return false;
		}
		
		Plot plot = city.getChunkPlotMap().get(location.getChunk());
		
		if (args.length < 1) {
			p.sendMessage(ChatColor.GREEN + "Plot Toggles");
			plot.getToggles().sendOptionValues(p);
			return false;
		}
		
		if (!((plot.getOwner() == null || plot.getOwner().equals(p.getUniqueId())) || city.getOfficers().contains(p.getUniqueId()))) {
			p.sendMessage(ChatColor.RED + "You must either own the plot or be a city Officer to change plot toggles.");
			return false;
		}
		
		if (!CivilizedToggles.isValidString(args[0])) {
			p.sendMessage(ChatColor.RED + "Invalid toggle type.");
			p.sendMessage(ChatColor.DARK_RED + "Valid toggle types: " + CivilizedToggles.getOptionsString());
			return false;
		}
		
		boolean current = plot.getToggles().getOption(args[0]);
		boolean setTo = false;
		
		if (args.length < 2) {
			setTo = !current;
		}
		else {
			
			switch(args[1].toUpperCase()) {
				case "YES":
				case "TRUE":
				case "Y":
				case "ENABLED":
				case "T":
					setTo = true;
					break;
				case "NO":
				case "FALSE":
				case "N":
				case "DISABLED":
				case "F":
					setTo = false;
					break;
				case "TOGGLE":
				case "OPPOSITE":
					setTo = !current;
					break;
				default:
					p.sendMessage(ChatColor.RED + "Invalid toggle option, it must be set to TRUE or FALSE.");
					return false;
			}
			
			
		}
		
		
		plot.getToggles().setOption(args[0], setTo);
		
		p.sendMessage(ChatColor.GREEN + "Set plot toggle " + ChatColor.AQUA + args[0].toUpperCase() + ChatColor.GREEN + " to " + ChatColor.AQUA + setTo);
		
		return false;
	}
}
