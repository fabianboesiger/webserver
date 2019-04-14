package server;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

public class Server {
	
	private static final int HANDLER_THREADS = 16;
	private static final String ALL_HANDLER = "ALL";

	
	private HttpServer httpServer;
	protected HashMap <String, LinkedList <Listener>> listeners;
	private long startingMillis;
	private long handles;
	private long bytes;
	protected Responder responder;
	protected SessionManager <?> sessionManager;
	
	public Server(int port, File publicFolder, Responder responder, SessionManager <?> sessionManager) throws IOException {
		System.out.println("Starting Server on Port " + port);
				
		startingMillis = System.currentTimeMillis();
		
	    listeners = new HashMap <String, LinkedList <Listener>> ();
	    handles = 0;
	    bytes = 0;
	    
	    this.responder = responder;
	    this.sessionManager = sessionManager;
	    
		// Set up Handler
		httpServer = HttpServer.create(new InetSocketAddress(port), 0);
		httpServer.setExecutor(Executors.newFixedThreadPool(HANDLER_THREADS));
		httpServer.createContext("/", new Handler (this));
	    httpServer.start();
	    
	    
	    Finder.find(publicFolder, (File file) -> {
	    	String path = file.getPath().replace(System.getProperty("file.separator"), "/");
			path = path.substring(publicFolder.getName().length());
			on("GET", path, (Request request) -> {
				return responder.file(file);
			});
	    });

	}
	
	public Server(int port, File publicFolder, Responder responder) throws IOException {
		this(port, publicFolder, responder, null);
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
	
    public long uptime() {
    	return System.currentTimeMillis() - startingMillis;
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

	public int sessionsCount() {
		if(sessionManager != null) {
			return sessionManager.sessionsCount();
		} else {
			return 0;
		}
	}
	
	public int activeCount() {
		if(sessionManager != null) {
			return sessionManager.activeCount();
		} else {
			return 0;
		}
	}
	

}
