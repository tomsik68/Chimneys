package sk.tomsik68.chimney;

import java.util.HashSet;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
			sender.sendMessage("Chimneys can't be created in console :P .");
			return true;
		}
		Player player = (Player)sender;
		if(args.length == 0){
			sender.sendMessage(ChatColor.RED+"/chimney [tgt | std] - Creates chimney at your target block(tgt) or block you're standing on(std)");
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("help")){
				sender.sendMessage(ChatColor.RED+"/chimney [tgt | std] - Creates chimney at your target block(tgt) or block you're standing on(std)");
			}else if(args[0].equalsIgnoreCase("std")){
				plugin.createChimney(player.getLocation().getBlock().getRelative(BlockFace.DOWN), 5, 15);
				sender.sendMessage(ChatColor.GREEN+"Chimney was create on block you're standing on");
			}else if(args[0].equalsIgnoreCase("tgt")){
				plugin.createChimney(player.getTargetBlock(ignored, 15), 5, 15);
				sender.sendMessage(ChatColor.GREEN+"Chimney was create on block you're aiming on");
			}
		}
		return true;
	}
}
