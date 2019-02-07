package application;

import java.io.IOException;
import java.util.HashMap;

import database.Database;
import database.structures.ObjectTemplateException;
import server.Request;
import server.Responder;
import server.Server;

public class Application {
	
	private Database database;
	private Responder responder;
	
	public Application() throws IOException {
		
		database = new Database();
		responder = new Responder(this);
		setup(database);
		
	}
	
	public void setup(Database database) throws IOException {
		
		Server server = new Server(database);
		
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
			return responder.render("signup.html");
		});
		
		server.on("POST", "/signup", (Request request) -> {
			User user = new User();
			user.parse(request.parameters);
			if(database.save(user)) {
				return responder.text("success");
			}
			return responder.redirect("/signup");
		});

	}
	
}
