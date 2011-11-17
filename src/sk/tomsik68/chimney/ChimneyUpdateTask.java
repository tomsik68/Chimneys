package sk.tomsik68.chimney;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class ChimneyUpdateTask implements Runnable {
	private final PluginChimney plugin;
	public ChimneyUpdateTask(PluginChimney pluginChimney) {
		plugin = pluginChimney;
	}

	@Override
	public void run() {
		Collection<Set<Chimney>> chimneys = Collections.synchronizedCollection(plugin.getChimneys());
		synchronized(chimneys){
			for(Set<Chimney> c : chimneys){
				for(Chimney chimney : c){
					chimney.update();
				}
			}
		}
	}
}
