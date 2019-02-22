package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import server.renderer.InterpreterException;
import server.renderer.Renderer;

public class Responder {
	
	public static final File VIEWS_FOLDER = new File("views");
	 
	private Map <String, Object> predefined;
	
	public Responder(Map <String, Object> predefined) {
		this.predefined = predefined;
	}
	
	public Response error(int code, String message, LinkedList <String> languages) throws IOException {
		HashMap <String, Object> variables = new HashMap <String, Object>();
		variables.put("code", new String("" + code));
		variables.put("message", new String(message));
		return render("error.html", languages, variables);
	}
	
	public Response text(String text) throws IOException {
		return new Response(text, "text/plain", 200, null, false);
	}
	
	public Response file(File file) throws IOException {
		return file(file, 200);
	}
	
	public Response file(File file, int statusCode) throws IOException {
		return new Response(new FileInputStream(file), file.length(), Files.probeContentType(Paths.get(file.getAbsolutePath())), statusCode, null, false);
	}
	
	public Response file(String name, int statusCode) throws IOException {
		return file(new File(name), statusCode);
	}
	
	public Response file(String name) throws IOException {
		return file(name, 200);
	}
	
	public Response render(String name) throws IOException {
		return render(new File(VIEWS_FOLDER.getName() + "/" + name), null, new HashMap <String, Object>());
	}
	
	public Response render(File file) throws IOException {
		return render(file, null, new HashMap <String, Object>());
	}
	
	public Response render(String name, Map <String, Object> variables) throws IOException {
		return render(new File(VIEWS_FOLDER.getName() + "/" + name), null, variables);
	}
	
	public Response render(File file, Map <String, Object> variables) throws IOException {
		return render(file, null, variables);
	}
	
	public Response render(String name, LinkedList <String> languages) throws IOException {
		return render(new File(VIEWS_FOLDER.getName() + "/" + name), languages, new HashMap <String, Object>());
	}
	
	public Response render(File file, LinkedList <String> languages) throws IOException {
		return render(file, languages, new HashMap <String, Object> ());
	}
	
	public Response render(String name, LinkedList <String> languages, Map <String, Object> variables) throws IOException {
		return render(new File(VIEWS_FOLDER.getName() + "/" + name), languages, variables);
	}
	
	public Response render(File file, LinkedList <String> languages, Map <String, Object> variables) throws IOException {
		variables.putAll(predefined);
		try {
			return new Response(Renderer.render(file, languages, variables), Files.probeContentType(Paths.get(file.getAbsolutePath())), 200, null, false);
		} catch (InterpreterException e) {
			e.printStackTrace();
			return error(500, "internal-server-error", languages);
		}
	}
	
	public Response redirect(String path) throws IOException {
		HashMap <String, String> responseHeaders = new HashMap <String, String> ();
		responseHeaders.put("Location", path);
		return new Response(null, 0, null, 302, responseHeaders, false);
	}
	
	public Response next() throws IOException {
		return new Response(null, 0, null, 0, null, true);
	}
	
}
