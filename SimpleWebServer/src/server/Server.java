package server;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

public class Server {
	
	public static final int HANDLER_THREADS = 4;
	public static final int SESSION_MAX_AGE = 60*60*24;
	public static final File PUBLIC_FOLDER = new File("public");
		
	private HttpServer server;
	
	public Server(int port) throws IOException {
		System.out.println("Server started on port " + port);
						
		server = HttpServer.create(new InetSocketAddress(port), 0);
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
				server.createContext(path, handler);
			}
		}
	}
	
}
