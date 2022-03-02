package com.drewgifford.civilized.command.subcommands.plot;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.permissions.CivilizedPermissions;
import com.drewgifford.civilized.permissions.PermissionLevel;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.plot.Plot;
import com.drewgifford.civilized.util.CityManager;

import net.md_5.bungee.api.ChatColor;

public class PlotPermissionsCommand extends CivilizedSubcommand {
	
	public PlotPermissionsCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
			p.sendMessage(ChatColor.GREEN + "Plot Permissions");
			plot.getPermissions().sendOptionValues(p);
			return false;
		}
		
		if (!((plot.getOwner() == null || plot.getOwner().equals(p.getUniqueId())) || city.getOfficers().contains(p.getUniqueId()))) {
			p.sendMessage(ChatColor.RED + "You must either own the plot or be a city Officer to change plot permissions.");
			return false;
		}
		
		if (!CivilizedPermissions.isValidString(args[0])) {
			p.sendMessage(ChatColor.RED + "Invalid permission type.");
			p.sendMessage(ChatColor.GRAY + "Valid permission types: " + CivilizedPermissions.getOptionsString());
			return false;
		}
		
		if (args.length < 2) {
			p.sendMessage(ChatColor.RED + "You need to provide which role to grant the permission to.");
			p.sendMessage(ChatColor.DARK_RED + "Valid roles: " + PermissionLevel.getOptionsString());
			return false;
		}
		
		PermissionLevel level = PermissionLevel.fromString(args[1]);
		
		if (level == null) {
			p.sendMessage(ChatColor.RED + "Invalid role.");
			p.sendMessage(ChatColor.DARK_RED + "Valid roles: " + PermissionLevel.getOptionsString());
			return false;
		}
		
		plot.getPermissions().setOption(args[0], level);
		
		p.sendMessage(ChatColor.GREEN + "Set plot permission " + ChatColor.AQUA + args[0].toUpperCase() + ChatColor.GREEN + " access to " + ChatColor.AQUA + level.toString());
		
		return false;
	}
}
