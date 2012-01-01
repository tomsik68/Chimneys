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

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;

public class CustomLocation implements Serializable {
	private static final long serialVersionUID = -7607191661093780590L;
	private final int x, y, z;
	private final UUID world;

	public CustomLocation(int x, int y, int z, UUID world) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
	}

	public CustomLocation(Block block) {
		this(block.getX(), block.getY(), block.getZ(), block.getWorld().getUID());
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return the z
	 */
	public int getZ() {
		return z;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CustomLocation) {
			CustomLocation cl = (CustomLocation) obj;
			return cl.getX() == x && cl.getY() == y && cl.getZ() == z && cl.getWorld().equals(world);
		}
		return false;
	}

	/**
	 * @return the world
	 */
	public UUID getWorld() {
		return world;
	}

	@Override
	public int hashCode() {
		return (int) ((x * 16 + y * 128 - z * 16 + world.getLeastSignificantBits() - world.getMostSignificantBits()));
	}

	public int getDistance(CustomLocation other) {
		int xvector = Math.abs(x - other.getX());
		int yvector = Math.abs(y - other.getY());
		int zvector = Math.abs(z - other.getZ());
		return xvector + yvector + zvector;
	}

	public Block getBlock() {
		return Bukkit.getWorld(world).getBlockAt(x, y, z);
	}
}
