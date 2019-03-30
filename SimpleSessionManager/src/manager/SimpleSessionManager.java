package manager;

import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import server.Session;
import server.SessionManager;

public class SimpleSessionManager implements SessionManager <String> {
	
	private int maxSessionAge;
	private ConcurrentHashMap <String, SimpleSession> sessions;

	public SimpleSessionManager(int maxSessionAge) {
		this.maxSessionAge = maxSessionAge;
	    sessions = new ConcurrentHashMap <String, SimpleSession> ();
	    
	    // Remove expired sessions and remove expired handle logs
	    Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				sessions.entrySet().removeIf(entry -> entry.getValue().expired());
			}
		}, 0, 1000);
	}
	
	@Override
	public Session <String> getSession(String key) {
		SimpleSession session = sessions.get(key);
		if(session != null) {
			session.update();
		}
		return session;
	}
	
	@Override
	public void removeSession(String key) {
		sessions.remove(key);
	}
	
	@Override
	public Session <String> createSession() {
		String key;
		do {
			key = generateKey(64);
		}while(sessions.containsKey(key));
		SimpleSession session = new SimpleSession(key, this);
		sessions.put(key, session);
		return session;
	}
	
	@Override
    public int sessionsCount() {
		return sessions.size();
	}
    
	@Override
    public int activeCount() {
    	int count = 0;
    	Iterator <Entry <String, SimpleSession>> iterator = sessions.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry <String, SimpleSession> pair = (Map.Entry <String, SimpleSession>) iterator.next();
            if(pair.getValue().active()) {
            	count++;
            }
        }
        return count;
    }

	@Override
	public int getMaxSessionAge() {
		return maxSessionAge;
	}

}
