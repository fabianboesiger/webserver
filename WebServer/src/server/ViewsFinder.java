package server;

import java.io.File;
import java.io.IOException;

import com.sun.net.httpserver.HttpServer;

public class ViewsFinder extends Finder {
	
	public static final File VIEWS_FOLDER = new File("views");
		
	public ViewsFinder(HttpServer httpServer) {
		super(VIEWS_FOLDER);
		
		this.httpServer = httpServer;
	}

	@Override
	protected void action(File file) throws IOException {
		
	}
	
}
