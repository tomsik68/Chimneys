package sk.tomsik68.chimney;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import sk.tomsik68.permsguru.EPermissionSystem;

public class PluginChimney extends JavaPlugin {
	private Map<CustomLocation, Set<Chimney>> chimneys = new HashMap<CustomLocation, Set<Chimney>>();
	private int frequency = 15, smokeCount = 5, radius = 48;
	// Block blacklist
	private Set<Byte> bblist = new HashSet<Byte>();
	private CBlockListener blockListener;
	private CPlayerListener playerListener;
	private Material wand;
	public EPermissionSystem perms; 
	private boolean rsDefOn = true;
	public PluginChimney() {

	}

	@Override
	public void onDisable() {
		DataManager.save(chimneys);
		getServer().getScheduler().cancelTasks(this);
		System.out.println("Chimneys " + getDescription().getVersion() + " is disabled");
	}

	@Override
	public void onEnable() {
		try {
			Class.forName("org.bukkit.craftbukkit.CraftServer");
		} catch (ClassNotFoundException e) {
			System.out.println("Chimneys can't be enabled. You're probably not running a craftbukkit server.");
			getServer().getPluginManager().disablePlugin(this);
		}
		blockListener = new CBlockListener(this);
		playerListener = new CPlayerListener(this);
		if (!getDataFolder().exists())
			getDataFolder().mkdir();
		Map<CustomLocation, Set<Chimney>> c = DataManager.load();
		if (c == null)
			DataManager.save(chimneys);
		else
			chimneys = c;
		getCommand("chimney").setExecutor(new ChimneyCommand(this));
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File(getDataFolder(),"config.yml"));
		if (!config.contains("chimney.smokes")) {
			config.set("chimney.smokes", 5);
			config.set("chimney.frequency", 15);
			config.set("chimney.radius", 48);
			config.set("chimney.wand", Material.STICK.name().toLowerCase());
			config.set("rs-def-on", true);
			config.set("perms", EPermissionSystem.OP.name());
			config.set("block-blacklist", bblist);
			try {
				config.save(new File(getDataFolder(), "config.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		frequency = config.getInt("chimney.smokes", 5);
		smokeCount = config.getInt("chimney.frequency", 15);
		radius = config.getInt("chimney.radius", 48);
		String wnd = config.getString("chimney.wand", "" + Material.STICK.name().toLowerCase());
		perms = EPermissionSystem.valueOf((String) config.get("perms","OP"));
		perms.getPermissor().setup(getServer());
		rsDefOn = config.getBoolean("rs-def-on");
		try {
			wand = Material.getMaterial(Integer.valueOf(wnd));
		} catch (Exception e) {
			wand = Material.valueOf(wnd.toUpperCase());
		}
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Type.REDSTONE_CHANGE, blockListener, Priority.Monitor, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.Monitor, this);
		pm.registerEvent(Type.BLOCK_BURN, blockListener, Priority.Monitor, this);
		pm.registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Monitor, this);

		System.out.println("Chimneys " + getDescription().getVersion() + " is enabled");
		int task = getServer().getScheduler().scheduleAsyncRepeatingTask(this, new ChimneyUpdateTask(this), frequency, frequency);
		if (task == -1) {
			System.out.println("[Chimneys] Task scheduling failed, plugin can't work.");
			getServer().getPluginManager().disablePlugin(this);
		}
		@SuppressWarnings("unchecked")
		List<String> list = config.getList("block-blacklist");
		if (list != null) {
			for (String s : list) {
				bblist.add(Byte.parseByte(s));
			}
		}
	}

	public synchronized void createChimney(Block block, BlockFace bf, boolean redstone) {
		if (bblist.contains(block.getTypeId()))
			return;
		CustomLocation loc = new CustomLocation(block);
		if (!chimneys.containsKey(loc))
			chimneys.put(loc, new HashSet<Chimney>());
		Set<Chimney> set = Collections.synchronizedSet(chimneys.get(new CustomLocation(block)));
		set.add(new Chimney(block, bf, smokeCount, redstone));
		chimneys.put(new CustomLocation(block), set);
	}

	public void deleteChimney(Block block) {
		chimneys.remove(new CustomLocation(block.getX(), block.getY(), block.getZ(), block.getWorld().getUID()));
	}

	public synchronized Collection<Set<Chimney>> getChimneys() {
		return chimneys.values();

	}

	public int getRadius() {
		return radius;
	}

	public Material getWand() {

		return wand;
	}

	public static PluginChimney instance() {
		return (PluginChimney) Bukkit.getPluginManager().getPlugin("Chimneys");
	}

	public void changeRS(Block b) {
		CustomLocation blockLocation = new CustomLocation(b);
		for (CustomLocation loc : chimneys.keySet()) {
			if (loc.getDistance(blockLocation) <= 1) {
				Block block = loc.getBlock();
				Set<Chimney> toUpdate = chimneys.get(loc);
				Set<Chimney> updated = new HashSet<Chimney>();
				for (Chimney chimney : toUpdate) {
					if ((rsDefOn && (block.isBlockPowered() || block.isBlockIndirectlyPowered() || block.getBlockPower() > 0)) || !rsDefOn) {
						chimney.setSmokeCount(smokeCount);
					} else {
						chimney.setSmokeCount(0);
					}
					toUpdate.remove(chimney);
					updated.add(chimney);
				}
				chimneys.put(loc, updated);
			}
		}
	}
}
