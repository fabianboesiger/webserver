# Web Server

## TODO
* ~~Remove "visitors per day" in server stats~~
* ~~Add hashing algorithm to database~~
* ~~Support for nested objects in database templates~~
* Add set command for templating engine
* Support for file upload
* New project for hashing
* New project for mail service
* Sending mails asynchronously

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
		Server server = new Server(responder, 8000);
		
		server.on("GET", "/", (Request request) -> {
			return responder.text("Hello World");
		});

	}

}
```

## Documentation
### WebServer
Dependencies: Renderer
#### Server (server.Server)
|Method|Description|
|---|---|
|`Server(Responder responder, int port)`|The constructor accepts a responder object and a port number to bind to.|
|`void on(String method, String path, ListenerAction listenerAction)`|Adds a new handler that listens on the specified path for the specified method. The ListenerAction interface gets executed and responds to the request.|
|`long uptime()`|Returns the uptime of the server in milliseconds.|
|`int sessionsCount()`|Returns the number of sessions.|
|`int activeCount()`|Returns the number of active sessions.|
|`double handlesPerDay()`|Returns the average handles per day.|
#### Responder (server.Responder)
### Renderer
### Mailer
