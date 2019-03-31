package manager;

import java.util.LinkedList;

import database.validator.Validator;
import server.Session;

public class SimpleSession implements Session <String> {
	
	private static final int ACTIVE = 5 * 60;
	
	private String id;
	private long lastConnect;
	private String object;
	private SimpleSessionManager simpleSessionManager;
	private LinkedList <Validator> flashes;
	
	public SimpleSession(String id, SimpleSessionManager simpleSessionManager) {
		this.id = id;
		this.simpleSessionManager = simpleSessionManager;
		update();
	}
	
	@Override
	public String getSessionId() {
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
	
	@Override
	public void addFlash(Object value) {
		flashes.add((Validator) value);
	}
	
	@Override
	public Object getFlash(String key) {
		for(Validator flash : flashes) {
			if(flash.getName().equals(key)) {
				flashes.remove(flash);
				return flash;
			}
		}
		return null;
	}
	
}
