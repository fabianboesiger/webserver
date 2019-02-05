package server;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import server.renderer.InterpreterException;
import server.renderer.Renderer;

public class Response {
	
	public static final String ENCODING = "UTF-8";
	public static final File VIEWS_FOLDER = new File("views");
	
	protected InputStream inputStream;
	protected long size;
	protected String contentType;
	protected int statusCode;
	protected HashMap <String, String> responseHeaders;
	boolean next;

	public Response(String text, String contentType, int statusCode, HashMap <String, String> responseHeaders, boolean next) throws IOException {
		this(new ByteArrayInputStream(text.getBytes(ENCODING)), text.getBytes(ENCODING).length, contentType, statusCode, responseHeaders, next);
	}

	public Response(InputStream inputStream, long size, String contentType, int statusCode, HashMap <String, String> responseHeaders, boolean next) {
		this.inputStream = inputStream;
		this.size = size;
		this.contentType = contentType;
		this.statusCode = statusCode;
		this.responseHeaders = responseHeaders;
		this.next = next;
	}
	
	public static Response error(String text, int statusCode) throws IOException { 
		return new Response(text, "text/plain", statusCode, null, false);
	}
	
	public static Response text(String text) throws IOException {
		return new Response(text, "text/plain", 200, null, false);
	}
	
	public static Response file(File file) throws IOException {
		return new Response(new FileInputStream(file), file.length(), Files.probeContentType(Paths.get(file.getAbsolutePath())), 200, null, false);
	}
	
	public static Response file(String name) throws IOException {
		return file(new File(name));
	}
	
	public static Response render(File file) throws IOException {
		try {
			return new Response(Renderer.render(file), Files.probeContentType(Paths.get(file.getAbsolutePath())), 200, null, false);
		} catch (InterpreterException e) {
			return error(e.getMessage(), 500);
		}
	}
	
	public static Response render(String name) throws IOException {
		return render(new File(VIEWS_FOLDER.getName() + "/" + name));
	}
	
	public static Response redirect(String path) throws IOException {
		HashMap <String, String> responseHeaders = new HashMap <String, String> ();
		responseHeaders.put("Location", path);
		return new Response(null, 0, null, 302, responseHeaders, false);
	}
	
	public static Response next() throws IOException {
		return new Response(null, 0, null, 0, null, true);
	}
	
}
