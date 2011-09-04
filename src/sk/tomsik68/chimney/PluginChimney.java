package sk.tomsik68.chimney;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginChimney extends JavaPlugin {
	private List<Chimney> chimneys = new ArrayList<Chimney>();
	private final CBlockListener blockListener;
	private int task;
	public PluginChimney() {
		blockListener = new CBlockListener(this);
	}

	@Override
	public void onDisable() {
		DataManager.save(chimneys);
		getServer().getScheduler().cancelTask(task);
		System.out.println("Chimneys "+getDescription().getVersion()+" is disabled");
	}

	@Override
	public void onEnable() {
		List<Chimney> c = DataManager.load();
		if(c == null) 
			DataManager.save(chimneys);
		else
			chimneys = c;
		getCommand("chimney").setAliases(Arrays.asList("chmn","ch"));
		getCommand("chimney").setExecutor(new ChimneyCommand(this));
		getServer().getPluginManager().registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.Monitor, this);	
		System.out.println("Chimneys "+getDescription().getVersion()+" is enabled");
		task = getServer().getScheduler().scheduleAsyncRepeatingTask(this, new ChimneyUpdateTask(this), 1L, 1L);
	}
	public synchronized boolean isChimney(Block block){
		for(Chimney chimney : chimneys){
			if(chimney.getX() == block.getX() && chimney.getY() == block.getY() && chimney.getZ() == block.getZ())
				return true;
			
			
		}
		return false;
	}
	public synchronized void createChimney(Block block, int smokes,int freq){
		chimneys.add(new Chimney(block, smokes, freq));
	}
	public synchronized void deleteChimney(Block block){
		for(Chimney chimney : chimneys){
			if(chimney.getX() == block.getX() && chimney.getY() == block.getY() && chimney.getZ() == block.getZ()){
				chimneys.remove(chimney);
				return;
			}
			
			
		}
	}
	public List<Chimney> getChimneys(){
		ArrayList<Chimney> result = new ArrayList<Chimney>();
		result.addAll(chimneys);
		return result;
		
	}
}
