package sk.tomsik68.chimney;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Chimney implements Serializable{
	private static final long serialVersionUID = -7589672671372115835L;
	private final int x,y,z;
	private int smokeCount = 0;
	private UUID worldId;
	public Chimney(Block block,int smokes){
		x = block.getX();
		y = block.getY();
		z = block.getZ();
		setSmokeCount(smokes);
		worldId = block.getWorld().getUID();
	}
	/**
	 * @param smokeCount the smokeCount to set
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
	public void update(){
		World world = Bukkit.getServer().getWorld(worldId);
		//if this chimney's world or chunk is not loaded, don't make smoke 
		if(world == null) return;
		//don't need to update if we haven't got the chunk loaded or if there aren't players
		if(!world.getChunkAt(new Location(world,x,y,z)).isLoaded() || world.getPlayers().isEmpty()) {
			return;
		}
		
		synchronized(world.getPlayers()){
			for(int i = 0;i<smokeCount;i++)
			{
				world.playEffect(new Location(world, x, y +1, z), Effect.SMOKE, 4);
			}
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
}
