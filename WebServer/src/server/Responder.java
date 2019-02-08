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
import server.renderer.container.ObjectContainer;

public class Responder {
	
	public static final File VIEWS_FOLDER = new File("views");
	private static final LinkedList <String> DEFAULT_LANGUAGES;

	static {
		DEFAULT_LANGUAGES = new LinkedList <String> ();
		DEFAULT_LANGUAGES.add("en");
	}
	 
	private Application application;
	private ObjectContainer predefined;
	
	public Responder(Application application, ObjectContainer predefined) {
		this.application = application;
		this.predefined = predefined;
	}
	
	public Response error(String text, int statusCode) throws IOException { 
		return new Response(application, text, "text/plain", statusCode, null, false);
	}
	
	public Response text(String text) throws IOException {
		return new Response(application, text, "text/plain", 200, null, false);
	}
	
	public Response file(File file) throws IOException {
		return file(file, 200);
	}
	
	public Response file(File file, int statusCode) throws IOException {
		return new Response(application, new FileInputStream(file), file.length(), Files.probeContentType(Paths.get(file.getAbsolutePath())), statusCode, null, false);
	}
	
	public Response file(String name, int statusCode) throws IOException {
		return file(new File(name), statusCode);
	}
	
	public Response file(String name) throws IOException {
		return file(name, 200);
	}
	
	public Response render(String name) throws IOException {
		return render(new File(VIEWS_FOLDER.getName() + "/" + name), DEFAULT_LANGUAGES, new ObjectContainer());
	}
	
	public Response render(File file) throws IOException {
		return render(file, DEFAULT_LANGUAGES, new ObjectContainer());
	}
	
	public Response render(String name, ObjectContainer variables) throws IOException {
		return render(new File(VIEWS_FOLDER.getName() + "/" + name), DEFAULT_LANGUAGES, variables);
	}
	
	public Response render(File file, ObjectContainer variables) throws IOException {
		return render(file, DEFAULT_LANGUAGES, variables);
	}
	
	public Response render(String name, LinkedList <String> languages) throws IOException {
		return render(new File(VIEWS_FOLDER.getName() + "/" + name), languages, new ObjectContainer());
	}
	
	public Response render(File file, LinkedList <String> languages) throws IOException {
		return render(file, languages, new ObjectContainer());
	}
	
	public Response render(String name, LinkedList <String> languages, ObjectContainer variables) throws IOException {
		return render(new File(VIEWS_FOLDER.getName() + "/" + name), languages, variables);
	}
	
	public Response render(File file, LinkedList <String> languages, ObjectContainer variables) throws IOException {
		variables.putAll(predefined);
		try {
			return new Response(application, Renderer.render(file, languages, variables), Files.probeContentType(Paths.get(file.getAbsolutePath())), 200, null, false);
		} catch (InterpreterException e) {
			e.printStackTrace();
			return error(e.getMessage(), 500);
		}
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
