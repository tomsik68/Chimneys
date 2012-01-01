/*    This file is part of Chimneys.

    Chimneys is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Chimneys is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Chimneys.  If not, see <http://www.gnu.org/licenses/>.*/
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
