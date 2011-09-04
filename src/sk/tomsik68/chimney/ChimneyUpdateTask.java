package sk.tomsik68.chimney;

public class ChimneyUpdateTask implements Runnable {
	private final PluginChimney plugin;
	public ChimneyUpdateTask(PluginChimney pluginChimney) {
		plugin = pluginChimney;
	}

	@Override
	public void run() {
		for(Chimney chimney : plugin.getChimneys()){
			chimney.update();
		}
	}
}
