# Web Server

## TODO
* Remove "visitors per day" in server stats
* Add hashing algorithm to database
* Support for nested objects in database templates
* Add set command for templating engine
* Support for file upload

## Getting Started
### Add Library
* Clone this repository
* Import the WebServer project
* Create a new Project for your website
* Add the WebServer project as a library for your project (For Eclipse: Right click your project, select Build Path, Configure Build Path, select the Projects tab, add the WebServer project and click apply)
### Hello World
```
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
```

## Documentation
