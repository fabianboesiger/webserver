package application;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import database.Database;
import server.Request;
import server.Responder;
import server.Server;

public class Application {
	
	private Database database;
	private Responder responder;
	private Server server;
	
	private HashMap <String, Object> predefined = new HashMap <String, Object>();

	
	public Application() throws IOException {		
		database = new Database();
		responder = new Responder(this, predefined);
		server = new Server(database, responder);
		setup();
		
	}
	
	public void setup() throws IOException {
		
		predefined.put("title", "Fälis Blog");

		server.on("GET", ".*", (Request request) -> {
			predefined.put("active-sessions", "" + server.activeCount());
			return responder.next();
		});
		
		server.on("GET", "/", (Request request) -> {
			return responder.render("index.html", request.languages);
		});
		
		server.on("GET", "/projects", (Request request) -> {
			return responder.render("projects.html", request.languages);
		});
		
		server.on("GET", "/server", (Request request) -> {
			return responder.render("server.html", request.languages);
		});
		
		server.on("GET", "/stats", (Request request) -> {
			long uptimeMillis = server.uptime();
			HashMap <String, Object> variables = new HashMap <String, Object> ();
			variables.put("uptime", String.format("%02d", uptimeMillis/1000/60/60) + ":" + String.format("%02d", uptimeMillis/1000/60%60) + ":" + String.format("%02d", uptimeMillis/1000%60));
			variables.put("sessions", "" + server.sessionsCount());
			variables.put("active-sessions", "" + server.activeCount());
			variables.put("handles-per-hour", "" + server.handlesPerHour());
			variables.put("visitors-per-hour", "" + server.visitorsPerHour());
			return responder.render("stats.html", request.languages, variables);
		});
		
		server.on("GET", "/signup", (Request request) -> {
			return responder.render("signup.html", request.languages, request.session.getFlashAsMap("errors"));
		});
		
		server.on("POST", "/signup", (Request request) -> {
			User user = new User();
			System.out.println("!1");
			user.setFromStringMap(request.parameters);
			LinkedList <String> errors = new LinkedList <String> ();
			System.out.println("!2");

			if(user.validate(errors)) {
				System.out.println("!3");

				if(database.save(user)) {
					return responder.text("success");
				}
			}
			System.out.println("!4");

			
			request.session.addFlash("errors", "username-taken");
			return responder.redirect("/signup");
		});
		
		server.on("GET", "/signup", (Request request) -> {
			return responder.text(request.session.getFlash("errors").toString());
		});
		
	}
	
}
