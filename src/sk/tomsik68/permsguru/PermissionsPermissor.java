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
package sk.tomsik68.permsguru;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class PermissionsPermissor implements Permissor {
	private PermissionHandler handler;
	public PermissionsPermissor() {

	}

	@Override
	public boolean has(Player player, String node) {
		if(handler == null) {
			return player.isOp();
		}
		return handler.has(player, node);
	}

	@Override
	public boolean setup(Server server) {
		Plugin test = server.getPluginManager().getPlugin("Permissions");
		if(test != null && test instanceof Permissions){
			handler = ((Permissions)test).getHandler();
			System.out.println("[Chimneys] Hooked into Permissions");
			return true;
		}
		System.out.println("[Chimneys] Permissions not found. Please set perms in config.yml to \"OP\" or \"Server\"");
		return false;
	}
}
