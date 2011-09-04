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
	private int delay = 0;
	private long updateTimestamp = 0;
	private UUID worldId;
	public Chimney(Block block,int smokes,int del){
		x = block.getX();
		y = block.getY();
		z = block.getZ();
		setSmokeCount(smokes);
		setDelay(del);
		worldId = block.getWorld().getUID();
		updateTimestamp = block.getWorld().getFullTime() + 80L;
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
	/**
	 * @param frequency the frequency to set
	 */
	public void setDelay(int frequency) {
		this.delay = frequency;
	}
	/**
	 * @return the frequency
	 */
	public int getDelay() {
		return delay;
	}
	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(long lastUpdate) {
		this.updateTimestamp = lastUpdate;
	}
	/**
	 * @return the lastUpdate
	 */
	public long getLastUpdate() {
		return updateTimestamp;
	}
	public void update(){
		World world = Bukkit.getServer().getWorld(worldId);
		//if this chimney's world or chunk is not loaded, don't make smoke 
		if(world == null) return;
		if(!world.getChunkAt(new Location(world,x,y,z)).isLoaded()) {
			updateTimestamp = world.getFullTime() + delay;
		}
		if(updateTimestamp + delay >= world.getFullTime()){
			for(int i =0;i<smokeCount;i++){
				world.playEffect(new Location(world, x, y +1, z), Effect.SMOKE, 4);
			}
			updateTimestamp += delay;
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
