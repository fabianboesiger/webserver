package server;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Response {
	
	private static final String ENCODING = "UTF-8";
	private static final File VIEWS_FOLDER = new File("views");
	
	protected InputStream inputStream;
	protected long size;
	protected String contentType;
	protected int statusCode;
	protected HashMap <String, String> responseHeaders;

	public Response(String text, String contentType, int statusCode, HashMap <String, String> responseHeaders) throws IOException {
		this(new ByteArrayInputStream(text.getBytes(ENCODING)), text.getBytes(ENCODING).length, contentType, statusCode, responseHeaders);
	}

	public Response(InputStream inputStream, long size, String contentType, int statusCode, HashMap <String, String> responseHeaders) {
		this.inputStream = inputStream;
		this.size = size;
		this.contentType = contentType;
		this.statusCode = statusCode;
		this.responseHeaders = responseHeaders;
	}
	
	public static Response error(String text, int statusCode) throws IOException { 
		return new Response(text, "text/plain", statusCode, null);
	}
	
	public static Response text(String text) throws IOException {
		return new Response(text, "text/plain", 200, null);
	}
	
	public static Response file(File file) throws IOException {
		return new Response(new FileInputStream(file), file.length(), Files.probeContentType(Paths.get(file.getAbsolutePath())), 200, null);
	}
	
	public static Response file(String name) throws IOException {
		return file(new File(name));
	}
	
	public static Response render(File file) throws IOException {
		return new Response(new FileInputStream(file), file.length(), Files.probeContentType(Paths.get(file.getAbsolutePath())), 200, null);
	}
	
	public static Response render(String name) throws IOException {
		return render(new File(VIEWS_FOLDER.getName() + "/" + name));
	}
	
	public static Response redirect(String path) throws IOException {
		HashMap <String, String> responseHeaders = new HashMap <String, String> ();
		responseHeaders.put("Location", path);
		return new Response(null, 0, null, 302, responseHeaders);
	}
	
}
