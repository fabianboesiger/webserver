package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.sun.net.httpserver.Headers;

public interface SessionManager <T> {
	
	public static final String SESSION_ID_COOKIE_NAME = "session-id";
	
	public static Random random = new Random();
	
	public default String generateKey(int length) {
    	String output = "";
    	for(int i = 0; i < length; i++) {
    		int r = random.nextInt(26*2+10)+48;
    		if(r > 57) {
    			r += 7;
    		}
    		if(r > 90) {
    			r += 6;
    		}
    		output += (char) r;
    	}
    	return output;
    }
	
	public Session <T> getSession(String key);
	public void removeSession(String key);
	public Session <T> createSession();
    public int sessionsCount();
    public int activeCount();
	public int getMaxSessionAge();
	
	public default Session <T> getSession(Headers requestHeaders, Headers responseHeaders) {

		List <String> requestCookies = requestHeaders.get("Cookie");
 		ArrayList <String> responseCookies = new ArrayList <String> ();
 		String sessionId = null;

 		if(requestCookies != null) {
 		    for(String cookie : requestCookies) {
 		    	String sessionIdValue = Handler.getValue(SESSION_ID_COOKIE_NAME, cookie);
 		    	if(sessionIdValue != null) {
 		    		sessionId = sessionIdValue;
 		    	}
 		    }
 		}

 		Session <T> session = null;
 		if(sessionId != null) {
 			session = getSession(sessionId);
 			if(session == null) {
 				session = createSession();
 			}
 		}else {
 			session = createSession();
 		}

 		responseHeaders.put("Set-Cookie", responseCookies);
 		responseCookies.add(SESSION_ID_COOKIE_NAME + "=" + session.getSessionId() + "; path=/; Max-Age=" + getMaxSessionAge());
 		
 		return session;
    }

	
}
