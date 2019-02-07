package server;

public class Session {
	
	protected static final int MAX_AGE = 7 * 24 * 60 * 60;
	private static final int ACTIVE = 60;
	
	private Server server;
	private String id;
	private long lastConnect;
	
	public Session(Server server, String id) {
		this.server = server;
		this.id = id;
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

}
