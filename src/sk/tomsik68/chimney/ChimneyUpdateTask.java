package sk.tomsik68.chimney;

import java.util.List;

public class ChimneyUpdateTask implements Runnable {
	private final PluginChimney plugin;
	public ChimneyUpdateTask(PluginChimney pluginChimney) {
		plugin = pluginChimney;
	}

	@Override
	public void run() {
		List<Chimney> chimneys= plugin.getChimneys();
		for(Chimney chimney : chimneys){
			chimney.update();
		}
	}
}
