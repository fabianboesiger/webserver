package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpExchange;

public class FileHandler extends Handler {
	
	private Response response;
	
	public FileHandler(File file) throws IOException {
		response = new Response(new FileInputStream(file), file.length(), Files.probeContentType(Paths.get(file.getAbsolutePath())));
	}
	
	public Response getResponse(HttpExchange httpExchange) {
		return response;
	}
	
}
