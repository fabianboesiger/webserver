package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Session {
	
	protected static final int MAX_AGE = 7 * 24 * 60 * 60;
	private static final int ACTIVE = 60;
	
	private String id;
	private long lastConnect;
	private HashMap <String, Object> flashes;
	
	public Session(String id) {
		this.id = id;
		flashes = new HashMap <String, Object> ();
		update();
	}
	
	public String getId() {
		return id;
	}
	
	public boolean expired() {
		if(System.currentTimeMillis() >= lastConnect + MAX_AGE * 1000) {
			return true;
		}
		return false;
	}
	
	public boolean active() {
		if(System.currentTimeMillis() < lastConnect + ACTIVE * 1000) {
			return true;
		}
		return false;
	}
	
	public void update() {
		lastConnect = System.currentTimeMillis();
	}
	
	public void addFlash(String key, Object value) {
		flashes.put(key, value);
	}
	
	public Object getFlash(String key) {
		return flashes.remove(key);
	}
	

}
