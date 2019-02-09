package application;

import java.io.IOException;

import database.Database;
import server.Request;
import server.Responder;
import server.Server;
import server.renderer.container.ObjectContainer;
import server.renderer.container.StringContainer;

public class Application {
	
	private Database database;
	private Responder responder;
	private Server server;
	
	public Application() throws IOException {
		
		ObjectContainer predefined = new ObjectContainer();
		predefined.put("title", new StringContainer("Fälis Blog"));
		
		database = new Database();
		responder = new Responder(this, predefined);
		server = new Server(database, responder);
		setup();
		
	}
	
	public void setup() throws IOException {
		
		server.on("GET", "/", (Request request) -> {
			return responder.render("index.html", request.languages);
		});
		
		server.on("GET", "/server", (Request request) -> {
			long uptimeMillis = server.uptime();
			return responder.text(
				"Uptime: \t" + String.format("%02d", uptimeMillis/1000/60/60) + ":" + String.format("%02d", uptimeMillis/1000/60%60) + ":" + String.format("%02d", uptimeMillis/1000%60) + "\n" +
				"Sessions: \t" + server.sessionsCount() + "\n" +
				"Active: \t" + server.activeCount() + "\n" +
				"Handles/Hour: \t" + server.handlesPerHour() + "\n" +
				"Visitors/Hour: \t" + server.visitorsPerHour()
			);
		});
		
		server.on("GET", "/signup", (Request request) -> {
			return responder.render("signup.html", request.session.getFlashAsObjectContainer("errors"));
		});
		
		server.on("POST", "/signup", (Request request) -> {
			User user = new User();
			user.parse(request.parameters);
			if(database.save(user)) {
				return responder.text("success");
			}
			request.session.addFlash("error", "user-already-exists");
			return responder.redirect("/signup");
		});
		
		server.on("GET", "/redirect", (Request request) -> {
			return responder.text(request.session.getFlash("errors").toString());
		});
		
	}
	
}
