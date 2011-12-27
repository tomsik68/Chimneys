package sk.tomsik68.chimney;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class CPlayerListener extends PlayerListener {
	private final PluginChimney plugin;

	public CPlayerListener(PluginChimney instance) {
		plugin = instance;
	}

	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (plugin.perms.getPermissor().has(event.getPlayer(), "chimneys.create.wand") && event.getItem() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getItem().getTypeId() == plugin.getWand().getId()) {
			plugin.createChimney(event.getClickedBlock(), event.getBlockFace(), false);
			event.setCancelled(true);
		}
	}
}
