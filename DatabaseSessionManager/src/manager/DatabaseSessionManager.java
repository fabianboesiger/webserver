package manager;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;

import database.Database;
import database.templates.ObjectTemplate;
import server.Session;
import server.SessionManager;

public class DatabaseSessionManager <T extends ObjectTemplate> implements SessionManager <T> {
	
	protected Database database;
	private int maxSessionAge;
	protected Supplier <T> supplier;

	public DatabaseSessionManager(Database database, int maxSessionAge, Supplier <T> supplier) {
		this.database = database;
		this.supplier = supplier;
		this.maxSessionAge = maxSessionAge;
		
		DatabaseSessionManager <T> self = this;
		
	    Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				database.deleteAll(DatabaseSession.class, (ObjectTemplate session) -> {
					return ((DatabaseSession <T>) session).expired();
				}, self);
			}
		}, 0, 10000);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Session <T> getSession(String key) {
		DatabaseSession <T> session = null;
		if((session = (DatabaseSession <T>) database.load(DatabaseSession.class, key, this)) != null) {
			session.update();
			database.update(session);
			return session;
		}
		return null;
	}
	
	@Override
	public void removeSession(String key) {
		database.delete(DatabaseSession.class, key);
	}
	
	@Override
	public Session <T> createSession() {
		String key;
		do {
			key = generateKey(64);
		} while (getSession(key) != null);
		DatabaseSession <T> session = new DatabaseSession <T> (this);
		session.setSessionId(key);
		database.save(session);
		return session;
	}
	
	@Override
    public int sessionsCount() {
		return database.getCount(DatabaseSession.class);
	}
    
	@SuppressWarnings("unchecked")
	@Override
    public int activeCount() {
		LinkedList <ObjectTemplate> active = database.loadAll(DatabaseSession.class, (ObjectTemplate session) -> {
			return ((DatabaseSession <T>) session).active();
		});
        return active.size();
    }

	@Override
	public int getMaxSessionAge() {
		return maxSessionAge;
	}
    
	
}
