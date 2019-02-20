package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Session {
	
	protected static final int MAX_AGE = 7 * 24 * 60 * 60;
	private static final int ACTIVE = 60;
	
	private String id;
	private long lastConnect;
	private HashMap <String, LinkedList <String>> flashes;
	
	public Session(String id) {
		this.id = id;
		flashes = new HashMap <String, LinkedList <String>> ();
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
	
	public void addFlash(String key, String value) {
		if(!flashes.containsKey(key)) {
			flashes.put(key, new LinkedList <String> ());
		}
		flashes.get(key).add(value);

	}
	
	public LinkedList <String> getFlash(String key) {
		return flashes.remove(key);
	}
	
	public Map <String, Object> getFlashAsMap(String key) {	
		ArrayList <String> arrayContainer = new ArrayList <String> ();
		LinkedList <String> messages = getFlash(key);
		if(messages != null) {
			for(String value : messages) {
				arrayContainer.add(value);
			}
		}
		HashMap <String, Object> objectContainer = new HashMap <String, Object> ();
		if(arrayContainer.size() > 0) {
			objectContainer.put(key, arrayContainer);
		}
		return objectContainer;
	}

}
