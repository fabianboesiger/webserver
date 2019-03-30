package manager;

import java.util.HashMap;

import server.Session;

public class SimpleSession implements Session <String> {
	
	private static final int ACTIVE = 5 * 60;
	
	private String id;
	private long lastConnect;
	private HashMap <String, Object> flashes;
	private String object;
	private SimpleSessionManager simpleSessionManager;
	
	public SimpleSession(String id, SimpleSessionManager simpleSessionManager) {
		this.id = id;
		this.simpleSessionManager = simpleSessionManager;
		flashes = new HashMap <String, Object> ();
		update();
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public boolean expired() {
		if(System.currentTimeMillis() >= lastConnect + simpleSessionManager.getMaxSessionAge() * 1000) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean active() {
		if(System.currentTimeMillis() < lastConnect + ACTIVE * 1000) {
			return true;
		}
		return false;
	}
	
	@Override
	public void update() {
		lastConnect = System.currentTimeMillis();
	}
	
	@Override
	public void addFlash(String key, Object value) {
		flashes.put(key, value);
	}
	
	@Override
	public Object getFlash(String key) {
		return flashes.remove(key);
	}
	
	@Override
	public void save(String object) {
		this.object = object;
	}
	
	@Override
	public void delete() {
		object = null;
	}
	
	@Override
	public String load() {
		return object;
	}
	
}
