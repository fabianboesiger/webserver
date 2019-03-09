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

public class Server {
	
	private static final int HANDLER_THREADS = 16;
	private static final File PUBLIC_FOLDER = new File("public");
	private static final String ALL_HANDLER = "ALL";

	
	private HttpServer httpServer;
	private ConcurrentHashMap <String, Session> sessions;
	protected HashMap <String, LinkedList <Listener>> listeners;
	private Random random;
	private long startingMillis;
	private long handles;
	private long bytes;
    Responder responder;
	
	public Server(Responder responder, int port) throws IOException {
		System.out.println("Starting server on port " + port);
		
		this.responder = responder;
		
		startingMillis = System.currentTimeMillis();
		
	    random = new Random();
	    listeners = new HashMap <String, LinkedList <Listener>> ();
	    sessions = new ConcurrentHashMap <String, Session> ();
	    handles = 0;
	    bytes = 0;
	    
		// Set up Handler
		httpServer = HttpServer.create(new InetSocketAddress(port), 0);
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
			}
		}, 0, 1000);
	}
	
	public void on(String method, String path, ListenerAction listenerAction) {
		Listener listener = new Listener(path, listenerAction);
		if(!listeners.containsKey(method)) {
			listeners.put(method, new LinkedList <Listener> ());
		}
		if(!method.equals(ALL_HANDLER)) {
			if(listeners.containsKey(ALL_HANDLER)) {
				listeners.get(method).addAll(listeners.get(ALL_HANDLER));
			}
		} else {
			for(Map.Entry <String, LinkedList <Listener>> entry : listeners.entrySet()) {
				if(!entry.getKey().equals(ALL_HANDLER)) {
					entry.getValue().add(listener);
				}
			}
		}
		listeners.get(method).add(listener);
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
	
    public String generateKey(int length) {
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
    
    protected void addHandleCount() {
    	handles++;
    }
    
    public double handlesPerDay() {
    	long uptime = uptime();
		return (double) handles / uptime * Math.min(uptime, 1000 * 60 * 60 * 24);
	}

	public void addByteCount(int bytesToAdd) {
		bytes += bytesToAdd;
	}
	
	public double bytesPerDay() {
    	long uptime = uptime();
		return (double) bytes / uptime * Math.min(uptime, 1000 * 60 * 60 * 24);
	}

}
