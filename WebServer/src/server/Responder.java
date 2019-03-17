package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import renderer.InterpreterException;
import renderer.Renderer;

public class Responder {
	
	public File viewsFolder;
	 
	private Map <String, Object> predefined;
	
	public Responder(File viewsFolder) {
		this(null, viewsFolder);
	}
	
	public Responder(Map <String, Object> predefined, File viewsFolder) {
		this.predefined = predefined;
		this.viewsFolder = viewsFolder;
	}
	
	public Response error(int code, String message, LinkedList <String> languages) throws IOException {
		return error(code, message, languages, null);
	}
	
	public Response error(int code, String message, LinkedList <String> languages, String error) throws IOException {
		HashMap <String, Object> variables = new HashMap <String, Object>();
		variables.put("code", code);
		variables.put("message", new String(message));
		if(error != null) {
			variables.put("error", error);
		}
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
		return render(new File(viewsFolder.getPath() + File.separator + name), new LinkedList <String> (), new HashMap <String, Object>());
	}
	
	public Response render(File file) throws IOException {
		return render(file, new LinkedList <String> (), new HashMap <String, Object>());
	}
	
	public Response render(String name, Map <String, Object> variables) throws IOException {
		return render(new File(viewsFolder.getPath() + File.separator + name), null, variables);
	}
	
	public Response render(File file, Map <String, Object> variables) throws IOException {
		return render(file, new LinkedList <String> (), variables);
	}
	
	public Response render(String name, LinkedList <String> languages) throws IOException {
		return render(new File(viewsFolder.getPath() + File.separator + name), languages, new HashMap <String, Object>());
	}
	
	public Response render(File file, LinkedList <String> languages) throws IOException {
		return render(file, languages, new HashMap <String, Object> ());
	}
	
	public Response render(String name, LinkedList <String> languages, Map <String, Object> variables) throws IOException {
		return render(new File(viewsFolder.getPath() + File.separator + name), languages, variables);
	}
	
	public Response render(File file, LinkedList <String> languages, Map <String, Object> variables) throws IOException {
		if(predefined != null) {
			HashMap <String, Object> copy = new HashMap <String, Object> (predefined);
			copy.keySet().removeAll(variables.keySet());
			variables.putAll(copy);
		}
		try {
			return new Response(Renderer.render(file, languages, variables, viewsFolder), Files.probeContentType(Paths.get(file.getAbsolutePath())), 200, null, false);
		} catch (InterpreterException e) {
			e.printStackTrace();
			StringWriter stringWriter = new StringWriter();
			e.printStackTrace(new PrintWriter(stringWriter));
			String error = stringWriter.toString();
			return error(500, "internal-server-error", languages, error);
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
