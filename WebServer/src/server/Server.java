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
	public static final File PUBLIC_FOLDER = new File("public");
		
	private HttpServer server;
	
	public Server() throws IOException {
		System.out.println("Server started on port "+PORT);
						
		server = HttpServer.create(new InetSocketAddress(PORT), 0);
	    server.setExecutor(Executors.newFixedThreadPool(HANDLER_THREADS));
	    findFiles(PUBLIC_FOLDER);
	    server.start();
	}
	
	private void findFiles(File folder) throws IOException {
		for(File file: folder.listFiles()) {
			if(file.isDirectory()) {
				findFiles(file);
			} else {
				String path = file.getPath().replace(System.getProperty("file.separator"), "/"); 
				if(path.matches(".*index.*")) {
					int cut = path.lastIndexOf("/");
					if(cut != -1) {
						path = path.substring(0, cut+1);
					}
				}
				path = path.substring(PUBLIC_FOLDER.getName().length());
				Handler handler = new Handler(file);
				System.out.println("Creating context "+path+" for "+file.getName());
				server.createContext(path, handler);
			}
		}
	}
	
}
