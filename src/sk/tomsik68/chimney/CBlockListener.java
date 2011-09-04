package sk.tomsik68.chimney;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

public class CBlockListener extends BlockListener {
	private final PluginChimney plugin;
	public CBlockListener(PluginChimney pc) {
		plugin = pc;
	}
	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		if(plugin.isChimney(event.getBlock())){
			plugin.deleteChimney(event.getBlock());
		}
	}
}
