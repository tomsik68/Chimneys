package sk.tomsik68.chimney;

import java.util.Collections;
import java.util.List;

import net.minecraft.server.Packet61;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Util {

	public static void playEffect(Location location, Effect effect, int data, int radius) {
		World world = location.getWorld();
		List<Player> players = Collections.synchronizedList(world.getPlayers());
		synchronized (players) {
			for (Player p : players) {
				int distance = (int) p.getLocation().distance(location);
				if (distance <= radius) {
					CraftPlayer player = (CraftPlayer) p;
					Packet61 packet = new Packet61(effect.getId(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), data);
					player.getHandle().netServerHandler.sendPacket(packet);
				}

			}
		}
	}
}
