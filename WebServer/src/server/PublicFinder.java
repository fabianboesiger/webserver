package server;

import java.io.File;
import java.io.IOException;

import com.sun.net.httpserver.HttpServer;

public class PublicFinder extends Finder {
	
	public static final File PUBLIC_FOLDER = new File("public");
	
	private HttpServer httpServer;
	
	public PublicFinder(HttpServer httpServer) {
		super(PUBLIC_FOLDER);
		
		this.httpServer = httpServer;
	}

	@Override
	protected void action(File file) throws IOException {
		String path = file.getPath().replace(System.getProperty("file.separator"), "/");
		if(path.matches(".*index.*")) {
			int cut = path.lastIndexOf("/");
			if(cut != -1) {
				path = path.substring(0, cut+1);
			}
		}
		path = path.substring(PUBLIC_FOLDER.getName().length());
		Handler handler = new FileHandler(file);
		httpServer.createContext(path, handler);
	}
	
}
