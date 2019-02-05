package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import database.Database;
import server.Response;
import server.Server;
import server.Session;

public class Application {
	
	public Application() throws IOException {
		
		setup();
		
	}
	
	public void setup() throws IOException {
		
		Server server = new Server(new Database());
		
		server.on("POST", "/", (Session session, ArrayList <String> groups, HashMap <String, String> parameters) -> {
			return Response.text("POST");
		});
		
		server.on("GET", "/", (Session session, ArrayList <String> groups, HashMap <String, String> parameters) -> {
			return Response.render("index.html");
		});
		
		server.on("GET", "/parameters", (Session session, ArrayList <String> groups, HashMap <String, String> parameters) -> {
			return Response.text(parameters.toString());
		});
		
		server.on("GET", "/(.*)/(.*)", (Session session, ArrayList <String> groups, HashMap <String, String> parameters) -> {
			return Response.text(groups.toString());
		});
		
		server.on("GET", "/redirect", (Session session, ArrayList <String> groups, HashMap <String, String> parameters) -> {
			return Response.redirect("/redirected");
		});
		
	}
	
}
