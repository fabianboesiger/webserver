package server;

import java.net.URI;

public class Listener {
	
	private String path;
	protected ListenerAction listenerAction;
	
	public Listener(String path, ListenerAction listenerAction) {
		this.path = path;
		this.listenerAction = listenerAction;
	}

	public boolean matches(URI uri) {
		return uri.toString().matches(path);
	}
	
}
