package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;

import application.Application;
import server.renderer.InterpreterException;
import server.renderer.Renderer;

public class Responder {
	
	public static final File VIEWS_FOLDER = new File("views");
	private static final LinkedList <String> DEFAULT_LANGUAGES;

	static {
		DEFAULT_LANGUAGES = new LinkedList <String> ();
		DEFAULT_LANGUAGES.add("en");
	}
	 
	private Application application;
	Renderer renderer;
	
	public Responder(Application application) {
		this.application = application;
		renderer = new Renderer(application);
	}
	
	public Response error(String text, int statusCode) throws IOException { 
		return new Response(application, text, "text/plain", statusCode, null, false);
	}
	
	public Response text(String text) throws IOException {
		return new Response(application, text, "text/plain", 200, null, false);
	}
	
	public Response file(File file) throws IOException {
		return new Response(application, new FileInputStream(file), file.length(), Files.probeContentType(Paths.get(file.getAbsolutePath())), 200, null, false);
	}
	
	public Response file(String name) throws IOException {
		return file(new File(name));
	}
	
	public Response render(File file) throws IOException {
		return render(file, DEFAULT_LANGUAGES);
	}
	
	public Response render(String name) throws IOException {
		return render(new File(VIEWS_FOLDER.getName() + "/" + name));
	}
	
	public Response render(File file, LinkedList <String> languages) throws IOException {
		try {
			return new Response(application, renderer.render(file, languages), Files.probeContentType(Paths.get(file.getAbsolutePath())), 200, null, false);
		} catch (InterpreterException e) {
			return error(e.getMessage(), 500);
		}
	}
	
	public Response render(String name, LinkedList <String> languages) throws IOException {
		return render(new File(VIEWS_FOLDER.getName() + "/" + name), languages);
	}
	
	public Response redirect(String path) throws IOException {
		HashMap <String, String> responseHeaders = new HashMap <String, String> ();
		responseHeaders.put("Location", path);
		return new Response(application, null, 0, null, 302, responseHeaders, false);
	}
	
	public Response next() throws IOException {
		return new Response(application, null, 0, null, 0, null, true);
	}
	
}
