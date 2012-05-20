/*
 * This file is part of Chimneys. Chimneys is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version. Chimneys is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with Chimneys. If not, see <http://www.gnu.org/licenses/>.
 */
package sk.tomsik68.chimney;

import java.util.HashSet;

public class ChimneyUpdateTask implements Runnable {
	private final PluginChimney plugin;

	public ChimneyUpdateTask(PluginChimney pluginChimney) {
		plugin = pluginChimney;
	}

	@Override
	public void run() {
		// nothing is more synchronized than 2 separated collections :)
		HashSet<Chimney> chimneys = new HashSet<Chimney>(plugin.getAllChimneys());
			for (Chimney chimney : chimneys) {
				chimney.update();
		}
	}
}
