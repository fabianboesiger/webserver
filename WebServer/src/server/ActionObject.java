package server;

import java.net.URI;

public class ActionObject {
	
	private String path;
	protected Action action;
	
	public ActionObject(String path, Action action) {
		this.path = path;
		this.action = action;
	}

	public boolean matches(URI uri) {
		return uri.toString().matches(path);
	}
	
}
