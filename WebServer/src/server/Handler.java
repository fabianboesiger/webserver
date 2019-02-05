package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Handler implements HttpHandler {
	
	private static final String SESSION_ID_COOKIE_NAME = "session-id";
	private static final int BUFFER_SIZE = 4096;
	
	Server server;
	
	public Handler(Server server) {
		this.server = server;
	}

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		
		long start = System.currentTimeMillis();
		
		Headers requestHeaders = httpExchange.getRequestHeaders();	
		Headers responseHeaders = httpExchange.getResponseHeaders();
    	Session session = getSession(requestHeaders, responseHeaders);
    	String method = httpExchange.getRequestMethod().toUpperCase();
    	URI uri = httpExchange.getRequestURI();
    	
    	HashMap <String, String> parameters = new HashMap <String, String> ();
    	HashMap <String, String> urlParameters = getParameters(uri.getQuery());
    	if(urlParameters != null) {
    		parameters.putAll(urlParameters);
    	}
    	HashMap <String, String> bodyParameters = getParameters((new BufferedReader(new InputStreamReader(httpExchange.getRequestBody(), Response.ENCODING))).readLine());
    	if(bodyParameters != null) {
    		parameters.putAll(bodyParameters);
    	}
    	
    	Response response = null;
	
    	LinkedList <Listener> listeners = server.listeners.get(method);
    	if(listeners != null) {
    		for(Listener listener : listeners) {
    			if(listener.matches(uri)) {
    				response = listener.listenerAction.act(session, listener.getGroups(uri), parameters);
    				if(response.next) {
    					response = null;
    				} else {
    					break;
    				}
    			}
    		}
    	}
    	
    	if(response == null) {
			response = Response.error("Not Found", 404);
		}
    	    	
    	try {
			
	    	if(response.contentType != null) {
	    		responseHeaders.set("Content-Type", response.contentType);
	    	}
	    	
	    	if(response.responseHeaders != null) {
	    		Iterator <Entry <String, String>> iterator = response.responseHeaders.entrySet().iterator();
	        	while (iterator.hasNext()) {
	            	Map.Entry <String, String> pair = (Map.Entry <String, String>) iterator.next();
	            	responseHeaders.set(pair.getKey(), pair.getValue());
	        	}
	    	}
	    	
	        OutputStream outputStream = httpExchange.getResponseBody();
	        
        	httpExchange.sendResponseHeaders(response.statusCode, response.size);
        	
        	if(response.inputStream != null) {
	        	byte[] buffer = new byte[BUFFER_SIZE];
		        int read;
			    while((read = response.inputStream.read(buffer)) != -1) {
			    	outputStream.write(buffer, 0, read);
			    }
			    response.inputStream.close();
        	}

		    outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	long time = System.currentTimeMillis() - start;
    	
		System.out.println(method + " " + uri.toString() + " " + response.statusCode + " " + time + "ms");
		
	}
	
	Session getSession(Headers requestHeaders, Headers responseHeaders) {
 		List<String> requestCookies = requestHeaders.get("Cookie");
 		ArrayList <String> responseCookies = new ArrayList <String> ();
 		String sessionId = null;
 		
 		if(requestCookies != null) {
 		    for(String cookie : requestCookies) {
 		    	String value = getValue(SESSION_ID_COOKIE_NAME, cookie);
 		    	if(value != null) {
 		    		sessionId = value;
 		    	}
 		    }
 		}

 		Session session = null;
 		if(sessionId != null) {
 			session = server.getSession(sessionId);
 			if(session == null) {
 				session = server.createSession();
 			}
 		}else {
 			session = server.createSession();
 		}
 		
 		responseHeaders.put("Set-Cookie", responseCookies);
 		responseCookies.add(SESSION_ID_COOKIE_NAME + "=" + session.getId() + "; path=/; Max-Age=" + Session.MAX_AGE);
    
 		return session;
    }
    
    private static String getValue(String key, String cookie) {
    	String[] pairs = cookie.split(";");
    	for(String pair : pairs) {
    		String[] splittedPair = pair.split("=");
    		if(key.equals(splittedPair[0].trim())) {
    			return splittedPair[1].trim();
    		}
    	}
    	return null;
    }
    
    public HashMap <String, String> getParameters(String query){
    	if(query != null) {
	    	HashMap <String, String> output = new HashMap <String, String> ();
	    	String[] pairs = query.split("&");
	    	for(String pair : pairs) {
	    		String[] splitted = pair.split("=");
	    		if(splitted.length == 2) {
	    			output.put(splitted[0], splitted[1]);
	    		}
	    	}
	    	return output;
    	}else {
    		return null;
    	}
    }

}
