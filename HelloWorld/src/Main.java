import java.io.IOException;

import server.Request;
import server.Responder;
import server.Server;

public class Main {

	public static void main(String[] args) throws IOException {
		
		Responder responder = new Responder();
		Server server = new Server(responder);
		
		server.on("GET", "/", (Request request) -> {
			return responder.text("Hello World");
		});

	}

}
