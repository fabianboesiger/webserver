package server.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpExchange;

public class FileHandler extends Handler {
	
	private File file;
	
	public FileHandler(File file) {
		this.file = file;
	}
	
	public Response getResponse(HttpExchange httpExchange) throws IOException {
		return new Response(new FileInputStream(file), file.length(), Files.probeContentType(Paths.get(file.getAbsolutePath())));
	}
	
}
