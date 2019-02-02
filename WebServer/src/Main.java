import java.io.IOException;

import database.Database;
import server.Response;
import server.Server;
import server.Session;

public class Main {

	public static void main(String[] args) throws IOException {
		Server server = new Server(new Database());
		
		server.on("GET", "/", (Session session) -> {
			return Response.render("index.html");
		});
		
		server.on("GET", "/redirect", (Session session) -> {
			return Response.redirect("/redirected");
		});
	}

}
