package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public abstract class Handler implements HttpHandler {
	
	protected class Response {
		
		InputStream inputStream;
		long size;
		String contentType;
		
		public Response(InputStream inputStream, long size, String contentType) {
			this.inputStream = inputStream;
			this.size = size;
			this.contentType = contentType;
		}
		
	}
	
	public Handler() {
	}

	@Override
	public void handle(HttpExchange httpExchange) {
		
		try {
			Response response = getResponse(httpExchange);
			
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

	protected abstract Response getResponse(HttpExchange httpExchange);
	
}
