package sk.tomsik68.chimney;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class CBlockListener extends BlockListener {
	private final PluginChimney plugin;

	public CBlockListener(PluginChimney pc) {
		plugin = pc;
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		plugin.deleteChimney(event.getBlock());
	}

	@Override
	public void onBlockRedstoneChange(BlockRedstoneEvent event) {
		plugin.changeRS(event.getBlock());
	}

	@Override
	public void onBlockBurn(BlockBurnEvent event) {
		plugin.deleteChimney(event.getBlock());
	}
}
