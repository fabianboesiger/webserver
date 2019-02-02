package server;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Response {
	
	private static final String ENCODING = "UTF-8";
	
	InputStream inputStream;
	long size;
	String contentType;
	
	public Response(InputStream inputStream, long size, String contentType) {
		this.inputStream = inputStream;
		this.size = size;
		this.contentType = contentType;
	}
	
	public static Response text(String text) throws IOException {
		return new Response(new ByteArrayInputStream(text.getBytes(ENCODING)), text.getBytes(ENCODING).length, "text/plain");
	}
	
	public static Response file(File file) throws IOException {
		return new Response(new FileInputStream(file), file.length(), Files.probeContentType(Paths.get(file.getAbsolutePath())));
	}
	
	public static Response file(String name) throws IOException {
		return file(new File(name));
	}
	
}
