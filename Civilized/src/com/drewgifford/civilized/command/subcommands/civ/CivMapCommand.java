package com.drewgifford.civilized.command.subcommands.civ;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.player.CivilizedPlayer;

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
		CivilizedPlayer cp = CivilizedPlayer.getCivilizedPlayer(p);
		
		
		int radius = 10;
		World world = p.getWorld();
		Chunk chunk = p.getLocation().getChunk();
		
		String topBorder = "";
		for(int x = -radius; x < radius + 1; x++) {
			topBorder += "-";
		}
		topBorder = '+' + topBorder + '+';
		p.sendMessage(ChatColor.GRAY + topBorder);
		
		
		for (int z = -1 * Math.round(radius/2); z < Math.round(radius/2) + 1; z++) {
			String line = ChatColor.GRAY + "❙❙";
			for (int x = -radius; x < radius + 1; x++) {
				
				String add = ChatColor.DARK_GRAY + "-";
				boolean center = false;
				
				if (x == 0 && z == 0) center = true;
				
				if (center) add = ChatColor.WHITE + "O";
				
				boolean foundChunk = false;
				
				for (City city : Civilized.cities) {
					
					for (Chunk c : city.getChunks()) {
						
						if(world.getChunkAt(chunk.getX() + x, chunk.getZ() + z).equals(c)) {
							foundChunk = true;
							if (city.equals(cp.getCity())) {
								add = ChatColor.GREEN + "/";
								if (center) add = ChatColor.GREEN + "O";
							}
							else {
								add = ChatColor.YELLOW + "=";
								if (center) add = ChatColor.YELLOW + "O";
							}
							break;
						}
						
					}
					
					if (foundChunk) break;
				}
				
				line += add;
			}
			p.sendMessage(line + ChatColor.GRAY + "❙❙");
		}
		
		p.sendMessage(ChatColor.GRAY + topBorder);
		
		
		
		
		
		return false;
	}

}
