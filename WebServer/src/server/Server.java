package server;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

import database.Database;

public class Server {
	
	private static final int PORT = 8000;
	private static final int HANDLER_THREADS = 4;
	private static final File PUBLIC_FOLDER = new File("public");
	
	private HttpServer httpServer;
	private HashMap <String, Session> sessions;
	protected HashMap <String, LinkedList <Listener>> listeners;
	private Random random;
	
	public Server(Database database) throws IOException {
		System.out.println("Starting server on port "+PORT);
		
	    random = new Random();
	    listeners = new HashMap <String, LinkedList <Listener>> ();
	    sessions = new HashMap <String, Session> ();
		
		// Set up Handler
		httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
		httpServer.setExecutor(Executors.newFixedThreadPool(HANDLER_THREADS));
		httpServer.createContext("/", new Handler(this));
	    httpServer.start();
	    
	    Finder.find(PUBLIC_FOLDER, (File file) -> {
	    	String path = file.getPath().replace(System.getProperty("file.separator"), "/");
			path = path.substring(PUBLIC_FOLDER.getName().length());
			on("GET", path, (Session session) -> {
				return Response.file(file);
			});
	    });
	    
	    // Remove expired sessions
	    Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				sessions.entrySet().removeIf(entry -> entry.getValue().expired());
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
		return sessions.get(key);
	}
	
	protected void removeSession(String key) {
		sessions.remove(key);
	}
	
	protected Session createSession() {
		String key;
		do {
			key = generateKey(64);
		}while(sessions.containsKey(key));
		Session session = new Session(this, key);
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

}
