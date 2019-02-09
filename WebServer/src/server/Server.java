package server;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

import database.Database;

public class Server {
	
	private static final int PORT = 8000;
	private static final int HANDLER_THREADS = 4;
	private static final File PUBLIC_FOLDER = new File("public");
	private static final int STATISTICS_MAX_AGE = 60 * 60;
	
	private HttpServer httpServer;
	private ConcurrentHashMap <String, Session> sessions;
	protected HashMap <String, LinkedList <Listener>> listeners;
	private Random random;
	private long startingMillis;
	protected LinkedList <Long> handles;
	protected LinkedList <Long> visitors;
    Responder responder;
	
	public Server(Database database, Responder responder) throws IOException {
		System.out.println("Starting server on port " + PORT);
		
		this.responder = responder;
		
		startingMillis = System.currentTimeMillis();
		
	    random = new Random();
	    listeners = new HashMap <String, LinkedList <Listener>> ();
	    sessions = new ConcurrentHashMap <String, Session> ();
	    handles = new LinkedList <Long> ();
	    visitors = new LinkedList <Long> ();
	    
		// Set up Handler
		httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
		httpServer.setExecutor(Executors.newFixedThreadPool(HANDLER_THREADS));
		httpServer.createContext("/", new Handler(this));
	    httpServer.start();
	    
	    
	    Finder.find(PUBLIC_FOLDER, (File file) -> {
	    	String path = file.getPath().replace(System.getProperty("file.separator"), "/");
			path = path.substring(PUBLIC_FOLDER.getName().length());
			on("GET", path, (Request request) -> {
				return responder.file(file);
			});
	    });
	    
	    // Remove expired sessions and remove expired handle logs
	    Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				sessions.entrySet().removeIf(entry -> entry.getValue().expired());
				
				long removeTime = System.currentTimeMillis() - STATISTICS_MAX_AGE * 1000;
				while(handles.peek() != null && handles.peek() < removeTime) {
					handles.remove();
				}
				while(visitors.peek() != null && visitors.peek() < removeTime) {
					visitors.remove();
				}
			}
		}, 0, 1000);
	}
	
	public void on(String method, String path, ListenerAction listenerAction) {
		if(!listeners.containsKey(method)) {
			listeners.put(method, new LinkedList <Listener> ());
		}
		listeners.get(method).add(new Listener(path, listenerAction));
	}
	
	protected Session getSession(String key) {
		Session session = sessions.get(key);
		if(session != null) {
			session.update();
		}
		return session;
	}
	
	protected void removeSession(String key) {
		sessions.remove(key);
	}
	
	protected Session createSession() {
		String key;
		do {
			key = generateKey(64);
		}while(sessions.containsKey(key));
		Session session = new Session(key);
		sessions.put(key, session);
		return session;
	}
	
    private String generateKey(int length) {
    	String output = "";
    	for(int i = 0; i < length; i++) {
    		int r = random.nextInt(26*2+10)+48;
    		if(r > 57) {
    			r += 7;
    		}
    		if(r > 90) {
    			r += 6;
    		}
    		output += (char) r;
    	}
    	return output;
    }
    
    public long uptime() {
    	return System.currentTimeMillis() - startingMillis;
    }
    
    public int sessionsCount() {
		return sessions.size();
	}
    
    public int activeCount() {
    	int count = 0;
    	Iterator <Entry <String, Session>> iterator = sessions.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry <String, Session> pair = (Map.Entry <String, Session>) iterator.next();
            if(pair.getValue().active()) {
            	count++;
            }
        }
        return count;
    }
    
    public int handlesPerHour() {
		return (int) ((double) handles.size() / STATISTICS_MAX_AGE  * 60 * 60);
	}
    
    public int visitorsPerHour() {
		return (int) ((double) visitors.size() / STATISTICS_MAX_AGE  * 60 * 60);
	}

}
