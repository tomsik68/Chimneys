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
