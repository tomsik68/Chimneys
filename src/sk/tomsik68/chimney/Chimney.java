/*
 * This file is part of Chimneys. Chimneys is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version. Chimneys is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with Chimneys. If not, see <http://www.gnu.org/licenses/>.
 */
package sk.tomsik68.chimney;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

/** Used to load an old files.
 * 
 * @author Tomsik68
 *
 */
public class Chimney implements Serializable {
	private static final long serialVersionUID = 5486728493060322647L;
	private int x, y, z;
	//unused, but kept not to destroy save structure
	private int face;
	private int smokeCount;
	private UUID worldId;
	private boolean redstone;
	public Chimney(Block block, /*BlockFace bf,*/ int smokes, boolean redstone) {
		face = 0;
		x = block.getX();
		y = block.getY();
		z = block.getZ();
		/*switch (bf) {
			case EAST:
				face = 1;
			break;
			case WEST:
				face = 7;
			break;
			case NORTH:
				face = 3;
			break;
			case SOUTH:
				face = 5;
			break;
			default:
				face = 4;
			break;
		}*/
		setSmokeCount(smokes);
		worldId = block.getWorld().getUID();
		this.redstone = redstone;
	}

	public Chimney(int x, int y, int z, int face, int smokes, boolean redstone, UUID world) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.face = face;
		this.smokeCount = smokes;
		this.redstone = redstone;
		worldId = world;
	}

	/**
	 * 
	 * @param smokeCount
	 *            the smokeCount to set
	 */
	public void setSmokeCount(int smokeCount) {
		this.smokeCount = smokeCount;
	}

	/**
	 * @return the smokeCount
	 */
	public int getSmokeCount() {
		return smokeCount;
	}

	public void update() {
		World world = Bukkit.getServer().getWorld(worldId);
		// if this chimney's world or chunk is not loaded, don't make smoke
		if (world == null)
			return;
		// don't need to update if we haven't got the chunk loaded or if there aren't players
		if (!world.getChunkAt(new Location(world, x, y, z)).isLoaded() || !Util.hasPlayers(world)) {
			return;
		}
		Location loc = new Location(world, x, y+1, z);
		for (int i = 0; i < smokeCount; i++) {
			Util.playEffect(loc, Effect.SMOKE, 0, ((PluginChimney) Bukkit.getPluginManager().getPlugin("Chimneys")).getRadius());
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public int getFace() {
		return face;
	}

	public UUID getWorldId() {
		return worldId;
	}

	/**
	 * @return the redstone
	 */
	public boolean isRedstone() {
		return redstone;
	}
}
