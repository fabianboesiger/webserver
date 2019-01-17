package server;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

public class Server {
	
	public static final int PORT = 8000;
	public static final int HANDLER_THREADS = 4;
	public static final int SESSION_MAX_AGE = 60*60*24;
	public static final File VIEWS_FOLDER = new File("views");
		
	private HttpServer httpServer;
	
	public Server() throws IOException {
		System.out.println("Server started on port "+PORT);
						
		httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
		httpServer.setExecutor(Executors.newFixedThreadPool(HANDLER_THREADS));
		httpServer.createContext("/", new MultiHandler());
	    new PublicFinder(httpServer);
	    httpServer.start();
	}

}
