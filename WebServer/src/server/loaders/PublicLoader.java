package server.loaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import server.handlers.Response;

public class PublicLoader extends Loader {

	public PublicLoader(File file) throws IOException {
		super(file);
	}

	@Override
	public Response load() throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file);
		return new Response(fileInputStream, file.length(), Files.probeContentType(Paths.get(file.getAbsolutePath())));
	}
	
	
	
}
