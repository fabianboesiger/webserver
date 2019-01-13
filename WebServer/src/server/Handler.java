package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import server.parser.Parser;

public class Handler implements HttpHandler {
	
	private static final String ENCODING = "UTF-8";
	
	private File file;
	private String contentType;
	
	public Handler(File file) throws IOException {
		this.file = file;
		contentType = Files.probeContentType(Paths.get(file.getAbsolutePath()));
	}

	@Override
	public void handle(HttpExchange httpExchange) {
		try {
	    	FileInputStream fileInputStream = new FileInputStream(file);
			
	    	if(contentType != null) {
	    		Headers responseHeaders = httpExchange.getResponseHeaders();
	    		responseHeaders.set("Content-Type", contentType);
	    	}
	    	
	        OutputStream outputStream = httpExchange.getResponseBody();
	        
	        if(contentType != null && contentType.startsWith("text")) {
	        	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, ENCODING));
	        	StringBuilder stringBuilder = new StringBuilder();
	            String line;
	            while((line = bufferedReader.readLine()) != null) {
	            	stringBuilder.append(line);
	            	stringBuilder.append("\n");
	            }
	            bufferedReader.close();
	            
	            byte[] data = Parser.parse(stringBuilder).getBytes(ENCODING);
	            
	            httpExchange.sendResponseHeaders(200, data.length);
	            outputStream.write(data);
	           
	        } else {
	        	httpExchange.sendResponseHeaders(200, file.length());
	        	
	        	byte[] buffer = new byte[4096];
		        int read;
			    while((read = fileInputStream.read(buffer)) != -1) {
			    	outputStream.write(buffer, 0, read);
			    }
	        }
		    
		    fileInputStream.close();
		    outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
