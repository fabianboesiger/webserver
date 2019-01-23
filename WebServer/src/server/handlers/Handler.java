package server.handlers;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public abstract class Handler implements HttpHandler {
	
	public Handler() {
	}

	@Override
	public void handle(HttpExchange httpExchange) {
		
		try {
			Response response = getResponse(httpExchange);
			
			if(response == null) {
				// TODO: Send error
			}
			
	    	if(response.contentType != null) {
	    		Headers responseHeaders = httpExchange.getResponseHeaders();
	    		responseHeaders.set("Content-Type", response.contentType);
	    	}
	    	
	        OutputStream outputStream = httpExchange.getResponseBody();
	        
        	httpExchange.sendResponseHeaders(200, response.size);
        	
        	byte[] buffer = new byte[4096];
	        int read;
		    while((read = response.inputStream.read(buffer)) != -1) {
		    	outputStream.write(buffer, 0, read);
		    }
		    
		    response.inputStream.close();
		    outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected abstract Response getResponse(HttpExchange httpExchange) throws IOException;
	
}
