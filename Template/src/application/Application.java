package application;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import database.Database;
import mailer.Mailer;
import manager.DatabaseSessionManager;
import responder.RenderResponder;
import server.Request;
import server.Server;
import user.User;
import user.UserManager;

public class Application {
	
	public static final int PORT = 8000;
	public static final int SESSION_MAX_AGE = 7 * 24 * 60 * 60;
	public static final File PUBLIC_FOLDER = new File("public");
	public static final File WEB_VIEWS = new File("views/web");
	public static final File MAIL_VIEWS = new File("views/mail");
	
	private Database database;
	private RenderResponder responder;
	private Mailer mailer;
	private Server server;
	
	private HashMap <String, Object> predefined = new HashMap <String, Object>();
	
	public Application() throws IOException {
		database = new Database();
		responder = new RenderResponder(predefined, WEB_VIEWS);
		mailer = new Mailer(predefined, MAIL_VIEWS);
		server = new Server(PORT, PUBLIC_FOLDER, responder, new DatabaseSessionManager <User> (database, SESSION_MAX_AGE, User::new));
		
		initializeRoutes();
		
		new UserManager(server, responder, database, mailer);
	}
	
	private void initializeRoutes() {
		
		predefined.put("title", "Template");
		
		server.on("ALL", ".*", (Request request) -> {
			User user = (User) request.session.load();
			if(user == null) {
				predefined.put("username", null);
			} else {
				predefined.put("username", user.getUsername());
			}
			return responder.next();
		});
		
		server.on("GET", "/", (Request request) -> {
			return responder.render("index.html", request.languages);
		});
		
		// TODO: Add additional routes
		
	}
	
}
