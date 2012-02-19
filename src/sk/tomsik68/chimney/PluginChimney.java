/*
 * This file is part of Chimneys. Chimneys is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version. Chimneys is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with Chimneys. If not, see <http://www.gnu.org/licenses/>.
 */
package sk.tomsik68.chimney;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import sk.tomsik68.permsguru.EPermissions;

public class PluginChimney extends JavaPlugin {
	private Map<CustomLocation, Chimney> chimneys = new HashMap<CustomLocation, Chimney>();
	private int frequency = 16, smokeCount = 5, radius = 48;
	// Block blacklist
	private final Set<Byte> bblist = new HashSet<Byte>();
	private Listener listener;
	private Material wand;
	public EPermissions perms;
	private boolean rsDefOn = true;
	private int task;
	private static final HashSet<Byte> ignored = new HashSet<Byte>();

	public PluginChimney() {

	}

	@Override
	public void onDisable() {
		getServer().getScheduler().cancelTask(task);
		System.out.println("Chimneys " + getDescription().getVersion()
				+ " is disabled");
	}

	@Override
	public void onEnable() {
		try {
			Class.forName("org.bukkit.craftbukkit.CraftServer");
		} catch (ClassNotFoundException e) {
			System.out
					.println("Chimneys can't be enabled. You're probably not running a craftbukkit server.");
			getServer().getPluginManager().disablePlugin(this);
		}
		listener = new ChimneysListener(this);
		getServer().getPluginManager().registerEvents(listener, this);
		if (!getDataFolder().exists())
			getDataFolder().mkdir();
		try{
			
		}catch(ClassCastException cce){
			try{
			System.out.println("[Chimneys] Detected old save format, but that's fine. Converting...");
			Map<CustomLocation, Set<Chimney>> c = DataManager.load();
			if (c != null) {
				for(Entry<CustomLocation,Set<Chimney>> entry : c.entrySet()){
					chimneys.put(entry.getKey(),entry.getValue().iterator().next());
				}
			}
			}catch (Exception e) {
				System.out.println("[Chimneys] Old save format conversion failed, but your data file should be fine. Here's stack trace: ");
				e.printStackTrace();
				System.out.println("[Chimneys] end of error");
			}
		}
		getCommand("chimney").setExecutor(this);
		FileConfiguration config = YamlConfiguration
				.loadConfiguration(new File(getDataFolder(), "config.yml"));
		if (!config.contains("chimney.smokes")) {
			config.set("chimney.smokes", 5);
			config.set("chimney.frequency", 15);
			config.set("chimney.radius", 48);
			config.set("chimney.wand", Material.STICK.name().toLowerCase());
			config.set("rs-def-on", true);
			config.set("perms", EPermissions.OP.name());
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
		wand = parseMaterial(config.getString("chimney.wand", ""
				+ Material.STICK.name().toLowerCase()));
		perms = EPermissions.valueOf((String) config.get("perms", "SP"));
		rsDefOn = config.getBoolean("rs-def-on");

		task = getServer().getScheduler().scheduleAsyncRepeatingTask(this,
				new ChimneyUpdateTask(this), frequency, frequency);
		if (task == -1) {
			System.out
					.println("[Chimneys] Task scheduling failed, plugin can't work.");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		List<Object> list = config.getList("block-blacklist");
		if (list != null) {
			for (Object o : list) {
				bblist.add(Byte.parseByte(o.toString()));
			}
		}

		System.out.println("Chimneys " + getDescription().getVersion()
				+ " is enabled");
	}

	private synchronized Chimney getChimneyAt(int x, int y, int z, UUID worldId) {
		return chimneys.get(new CustomLocation(x, y, z, worldId));
	}

	public synchronized boolean createChimney(Block block, boolean redstone) {
		if (bblist.contains(block.getTypeId()))
			return false;
		CustomLocation loc = new CustomLocation(block);
		if (getChimneyAt(loc) == null) {
			Chimney chimney = new Chimney(block, smokeCount, redstone);
			chimneys.put(loc, chimney);
			return true;
		}
		return false;
	}

	private synchronized Chimney getChimneyAt(CustomLocation loc) {
		return getChimneyAt(loc.getX(), loc.getY(), loc.getZ(), loc.getWorld());
	}

	public synchronized void deleteChimney(Block block) {
		chimneys.remove(new CustomLocation(block));
	}

	public synchronized Collection<Chimney> getAllChimneys() {
		return chimneys.values();
	}

	public int getRadius() {
		return radius;
	}

	public Material getWand() {

		return wand;
	}

	public synchronized void changeRS(Block b) {
		Set<CustomLocation> toUpdate = new HashSet<CustomLocation>();
		toUpdate.add(new CustomLocation(b.getRelative(BlockFace.DOWN)));
		toUpdate.add(new CustomLocation(b.getRelative(BlockFace.UP)));
		toUpdate.add(new CustomLocation(b.getRelative(BlockFace.WEST)));
		toUpdate.add(new CustomLocation(b.getRelative(BlockFace.EAST)));
		toUpdate.add(new CustomLocation(b.getRelative(BlockFace.NORTH)));
		toUpdate.add(new CustomLocation(b.getRelative(BlockFace.SOUTH)));
		for (CustomLocation loc : toUpdate) {
			if (getChimneyAt(loc) != null) {
				Block block = loc.getBlock();
				Chimney chimney = getChimneyAt(loc);
				if ((rsDefOn && (block.isBlockPowered()
						|| block.isBlockIndirectlyPowered() || block
						.getBlockPower() > 0)) || !rsDefOn) {
					chimney.setSmokeCount(smokeCount);
				} else {
					chimney.setSmokeCount(0);
				}
				updateChimney(loc, chimney);
			}
		}
	}

	private synchronized void updateChimney(CustomLocation loc, Chimney chimney) {
		chimneys.put(loc, chimney);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.GREEN
					+ "Chimneys can't be created in console :P .");
			return true;
		}
		Player player = (Player) sender;
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED
					+ "/chimney [tgt | std] - Creates chimney at your target block(tgt) or block you're standing on(std)");
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("help")
					|| args[0].equalsIgnoreCase("?")) {
				sender.sendMessage(ChatColor.RED
						+ "/chimney [tgt | std] - Creates chimney at your target block(tgt) or block you're standing on(std)");
				return true;
			} else if (args[0].equalsIgnoreCase("std")
					&& perms.has(player, "chimneys.create")) {
				if (createChimney(
						player.getLocation().getBlock()
								.getRelative(BlockFace.DOWN), false))
					sender.sendMessage(ChatColor.GREEN
							+ "Chimney was created on block you're standing on");
				else
					sender.sendMessage(ChatColor.RED
							+ "[Chimneys] Can't create chimney there.");
				return true;
			} else if (args[0].equalsIgnoreCase("tgt")
					&& perms.has(player, "chimneys.create")) {
				if (createChimney(player.getTargetBlock(ignored, 15), false))
					sender.sendMessage(ChatColor.GREEN
							+ "Chimney was created on block you're aiming on");
				else
					sender.sendMessage(ChatColor.RED
							+ "[Chimneys] Can't create chimney there.");
				return true;
			} else if (args[0].equalsIgnoreCase("wand")
					&& perms.has(player, "chimneys.wand.get")) {
				player.getInventory().addItem(new ItemStack(getWand()));
				sender.sendMessage(ChatColor.GREEN
						+ "There you go chimney wand! (Wand name is: "
						+ ChatColor.GRAY + getWand().name().toLowerCase() + ")");
				return true;
			}
		} else if (args.length == 2) {
			if (args[1].equalsIgnoreCase("rs")
					&& perms.has(player, "chimneys.create")) {
				if (args[0].equalsIgnoreCase("std")) {
					if (createChimney(player.getLocation().getBlock()
							.getRelative(BlockFace.DOWN), true))
						sender.sendMessage(ChatColor.GREEN
								+ "Redstone controlled chimney was created on block you're standing on");
					else
						sender.sendMessage(ChatColor.RED
								+ "[Chimneys] Can't create chimney there.");
				} else if (args[0].equalsIgnoreCase("tgt")) {
					if (createChimney(player.getTargetBlock(ignored, 15), true))
						sender.sendMessage(ChatColor.GREEN
								+ "Redstone controlled chimney was created on block you're aiming on");
					else
						sender.sendMessage(ChatColor.RED
								+ "[Chimneys] Can't create chimney there.");
				}
				return true;
			}
		}
		return false;
	}

	public static Material parseMaterial(String str) {
		Material result;
		try {
			result = Material.getMaterial(Integer.valueOf(str));
		} catch (Exception e) {
			try {
				result = Material.valueOf(str.toUpperCase());
			} catch (Exception e1) {
				System.out.println("[Chimneys] Unknown item id/name \"" + str
						+ "\". Using stick.");
				result = Material.STICK;
			}
		}
		return result;
	}

	static {
		ignored.add((byte) Material.SNOW.getId());
		ignored.add((byte) Material.AIR.getId());
		ignored.add((byte) Material.REDSTONE_TORCH_ON.getId());
		ignored.add((byte) Material.REDSTONE_TORCH_OFF.getId());
		ignored.add((byte) Material.TORCH.getId());

	}
}
