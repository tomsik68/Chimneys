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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.Entity;
import net.minecraft.server.Packet61WorldEvent;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Util {

	public static void playEffect(Location location, Effect effect, int data, int radius) {
		World world = location.getWorld();
		List<Player> players = getPlayers(world);
		synchronized (players) {
			for (Player p : players) {
				int distance = (int) p.getLocation().distance(location);
				if (distance <= radius) {
					CraftPlayer player = (CraftPlayer) p;
					Packet61WorldEvent packet = new Packet61WorldEvent(effect.getId(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), 4);
					try{
						player.getHandle().netServerHandler.sendPacket(packet);
					}catch(Exception e){
						//player has probably disconnected before this proccess - do nothing
					}
				}

			}
		}
	}
	public static List<Player> getPlayers(World w){
		CraftWorld world = (CraftWorld)w;
		List<Player> result = new ArrayList<Player>();
		@SuppressWarnings("unchecked")
		List<Entity> entities = new ArrayList<Entity>(world.getHandle().entityList);
		for(Entity e : entities){
			if(e != null && e.getBukkitEntity() != null && e.getBukkitEntity() instanceof Player){
				result.add((Player)e.getBukkitEntity());
			}
		}
		return result;
	}
	public static boolean hasPlayers(World w) {
		CraftWorld world = (CraftWorld)w;
		if(world.getHandle().entityList == null || world.getHandle().entityList.isEmpty())
			return false;
		@SuppressWarnings("unchecked")
		List<Entity> entities = new ArrayList<Entity>(world.getHandle().entityList);
		for(Entity e : entities){
			if(e != null && e.getBukkitEntity() != null && e.getBukkitEntity() instanceof Player){
				return true;
			}
		}
		return false;
	}
}
