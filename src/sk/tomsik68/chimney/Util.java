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
					Packet61WorldEvent packet = new Packet61WorldEvent(effect.getId(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), data);
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
			if(e.getBukkitEntity() != null && e.getBukkitEntity() instanceof Player){
				return true;
			}
		}
		return false;
	}
}
