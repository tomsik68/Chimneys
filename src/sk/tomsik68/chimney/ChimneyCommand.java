package sk.tomsik68.chimney;

import java.util.HashSet;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChimneyCommand implements CommandExecutor {
	private final PluginChimney plugin;
	private final HashSet<Byte> ignored = new HashSet<Byte>();
	public ChimneyCommand(PluginChimney instance) {
		plugin = instance;
		ignored.add((byte) Material.SNOW.getId());
		ignored.add((byte) Material.AIR.getId());
		ignored.add((byte) Material.REDSTONE_TORCH_ON.getId());
		ignored.add((byte) Material.REDSTONE_TORCH_OFF.getId());
		ignored.add((byte) Material.TORCH.getId());
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.GREEN+"Chimneys can't be created in console :P .");
			return true;
		}
		
		Player player = (Player)sender;
		if(args.length == 0){
			sender.sendMessage(ChatColor.RED+"/chimney [tgt | std] - Creates chimney at your target block(tgt) or block you're standing on(std)");
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("help")){
				sender.sendMessage(ChatColor.RED+"/chimney [tgt | std] - Creates chimney at your target block(tgt) or block you're standing on(std)");
			}else if(args[0].equalsIgnoreCase("std")){
				plugin.createChimney(player.getLocation().getBlock().getRelative(BlockFace.DOWN),BlockFace.UP,false);
				sender.sendMessage(ChatColor.GREEN+"Chimney was created on block you're standing on");
			}else if(args[0].equalsIgnoreCase("tgt")){
				plugin.createChimney(player.getTargetBlock(ignored, 15),BlockFace.UP,false);
				sender.sendMessage(ChatColor.GREEN+"Chimney was created on block you're aiming on");
			}else if(args[0].equalsIgnoreCase("wand")){
				player.getInventory().addItem(new ItemStack(plugin.getWand()));
				sender.sendMessage(ChatColor.GREEN+"There you go chimney wand! (Wand name is: "+plugin.getWand().name().toLowerCase()+")");
			}
		}else if(args.length == 2){
			if(args[1].equalsIgnoreCase("rs")){
				if(args[0].equalsIgnoreCase("std")){
					plugin.createChimney(player.getLocation().getBlock().getRelative(BlockFace.DOWN),BlockFace.UP,true);
					sender.sendMessage(ChatColor.GREEN+"Redstone controlled chimney was created on block you're standing on");
				}else if(args[0].equalsIgnoreCase("tgt")){
					plugin.createChimney(player.getTargetBlock(ignored, 15),BlockFace.UP,true);
					sender.sendMessage(ChatColor.GREEN+"Redstone controlled chimney was created on block you're aiming on");
				}
			}
		}
		return true;
	}

}
