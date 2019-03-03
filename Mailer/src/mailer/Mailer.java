package mailer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import renderer.InterpreterException;
import renderer.Renderer;

public class Mailer {
	
	public static final Charset ENCODING = StandardCharsets.UTF_8;
	protected static final File MAIL_DATA = new File("local/mail.txt");
	public static final File VIEWS_FOLDER = new File("views/mail");
	
	private Map <String, Object> predefined;
	
	public Mailer() {
		this(null);
	}
	
	public Mailer(Map <String, Object> predefined) {
		this.predefined = predefined;
	}
	
	public void send(String email, String subject, String name) {
		send(email, subject, name, new LinkedList <String> (), new HashMap <String, Object> ());
    }
	
	public void send(String email, String subject, String name, Map <String, Object> variables) {
		send(email, subject, name, new LinkedList <String> (), variables);
    }
	
	public void send(String email, String subject, String name, List <String> languages) {
		send(email, subject, name, languages, new HashMap <String, Object> ());
    }
	
	public void send(String email, String subject, String name, List <String> languages, Map <String, Object> variables) {
		if(predefined != null) {
			HashMap <String, Object> copy = new HashMap <String, Object> (predefined);
			copy.keySet().removeAll(variables.keySet());
			variables.putAll(copy);
		}
		try {
			new Sender(email, Renderer.render(new BufferedReader(new StringReader(subject)), languages, variables, VIEWS_FOLDER), Renderer.render(new File(VIEWS_FOLDER.getPath() + File.separator + name), languages, variables, VIEWS_FOLDER), "text/html");
		} catch (InterpreterException | IOException e) {
			e.printStackTrace();
		}
    }
	
	public void sendText(String email, String subject, String body) {
		new Sender(email, subject, body, "text/plain");
    }
	
	

}
