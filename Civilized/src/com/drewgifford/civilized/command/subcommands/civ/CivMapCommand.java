package com.drewgifford.civilized.command.subcommands.civ;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.plot.Plot;

import net.md_5.bungee.api.ChatColor;

public class CivMapCommand extends CivilizedSubcommand {

	public CivMapCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
		super(pl, label, aliases, permission, description);
	}

	@Override
	public boolean run(CommandSender sender, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can run this command.");
			return false;
		}
		Player p = (Player) sender;
		
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("on")) {
				sendMapUpdate(p);
				p.sendMessage(ChatColor.GREEN + "Automatic map updates enabled. Type /civ map off to disable.");
				Civilized.activeMaps.add(p.getUniqueId());
			}
			if (args[0].equalsIgnoreCase("off")) {
				p.sendMessage(ChatColor.RED + "Automatic map updates disabled. Type /civ map on to enable.");
				Civilized.activeMaps.remove(p.getUniqueId());
			}
		} else {
			sendMapUpdate(p);
		}
		
		
		
		return false;
	}
	
	public static void sendMapUpdate(Player p) {
		sendMapUpdate(p, p.getLocation());
	}
	
	public static void sendMapUpdate(Player p, Location loc) {
		int radius = 7;
		World world = loc.getWorld();
		Chunk chunk = loc.getChunk();
		CivilizedPlayer cp = CivilizedPlayer.getCivilizedPlayer(p);
		
		p.sendMessage(ChatColor.AQUA + "Map of nearby cities:");
		
		String topBorder = "";
		for(int x = -radius; x < radius + 1; x++) {
			topBorder += "-";
		}
		topBorder = '+' + topBorder + '+';
		p.sendMessage(ChatColor.GRAY + topBorder);
		
		
		for (int z = -1 * Math.round(radius/2); z < Math.round(radius/2) + 1; z++) {
			String line = ChatColor.GRAY + "❙❙";
			for (int x = -radius; x < radius + 1; x++) {
				
				
				char add_char = '-';
				ChatColor color = ChatColor.DARK_GRAY;
				
				boolean center = false;
				
				if (x == 0 && z == 0) center = true;
				
				if (center) {
					color = ChatColor.WHITE;
					add_char = 'O';
				}
				
				boolean foundChunk = false;
				
				for (City city : Civilized.cities) {
					
					for (Chunk c : city.getChunks()) {
						
						if(world.getChunkAt(chunk.getX() + x, chunk.getZ() + z).equals(c)) {
							foundChunk = true;
							
							if(!center) add_char = '/';
							
							Plot plot = city.getChunkPlotMap().get(c);
							
							if(plot.isForSale()) {
								if(!center) add_char = '$';
							}
							
							if(plot.getChunk().equals(city.getHomeChunk())) {
								if(!center) add_char = 'H';
							}
							
							if (city.equals(cp.getCity())) {
								color = ChatColor.GREEN;
							}
							else {
								color = ChatColor.YELLOW;
							}
							break;
						}
						
					}
					
					if (foundChunk) break;
				}
				
				line += color.toString() + add_char;
			}
			p.sendMessage(line + ChatColor.GRAY + "❙❙");
		}
		
		p.sendMessage(ChatColor.GRAY + topBorder);
	}

}
