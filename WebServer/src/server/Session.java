package server;

public class Session {
	
	protected static final int MAX_AGE = 60*60*24;
	
	private Server server;
	private String id;
	private long lastConnect;
	
	public Session(Server server, String id) {
		this.server = server;
		this.id = id;
		lastConnect = System.currentTimeMillis();
	}
	
	public String getId() {
		return id;
	}
	
	public boolean expired() {
		if(System.currentTimeMillis() > lastConnect + MAX_AGE * 1000) {
			return true;
		}
		return false;
	}

}
