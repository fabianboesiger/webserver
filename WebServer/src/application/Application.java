package application;

import java.io.IOException;
import java.util.HashMap;

import database.Database;
import server.Request;
import server.Responder;
import server.Server;

public class Application {
	
	private Database database;
	private Responder responder;
	private Server server;
	
	public Application() throws IOException {
		
		HashMap <String, Object> predefined = new HashMap <String, Object>();
		predefined.put("title", "Fälis Blog");
		
		database = new Database();
		responder = new Responder(this, predefined);
		server = new Server(database, responder);
		setup();
		
	}
	
	public void setup() throws IOException {
		
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
			variables.put("sessions-count", "" + server.sessionsCount());
			variables.put("active-count", "" + server.activeCount());
			return responder.render("stats.html", request.languages, variables);
		});
		
		server.on("GET", "/signup", (Request request) -> {
			return responder.render("signup.html", request.languages, request.session.getFlashAsMap("errors"));
		});
		
		server.on("POST", "/signup", (Request request) -> {
			User user = new User();
			user.parse(request.parameters);
			if(database.save(user)) {
				return responder.text("success");
			}
			request.session.addFlash("errors", "username-taken");
			return responder.redirect("/signup");
		});
		
		server.on("GET", "/signup", (Request request) -> {
			return responder.text(request.session.getFlash("errors").toString());
		});
		
	}
	
}
