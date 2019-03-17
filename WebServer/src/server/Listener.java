package server;

import java.net.URI;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Listener {
	
	private String path;
	protected ListenerAction listenerAction;
	
	public Listener(String path, ListenerAction listenerAction) {
		this.path = path;
		this.listenerAction = listenerAction;
	}

	public boolean matches(URI uri) {
		return cut(uri).matches(path);
	}
	
	public String cut(URI uri) {
		String uriPath = uri.toString(); 
		int questionmarkIndex = uriPath.indexOf("?");
		int hashIndex = uriPath.indexOf("#");
		int index = -1;
		if(questionmarkIndex != -1 && hashIndex != -1) {
			index = Math.min(hashIndex, questionmarkIndex);
		} else {
			if(questionmarkIndex != -1) {
				index = questionmarkIndex;
			} else
			if(hashIndex != -1) {
				index = hashIndex;
			}
		}
		if(index != -1) {
			uriPath = uriPath.substring(0, index);
		}
		return uriPath;
	}
	
	public ArrayList <String> getGroups(URI uri) {
		ArrayList <String> groups = new ArrayList <String> ();
		Pattern pattern = Pattern.compile(path);
		Matcher matcher = pattern.matcher(cut(uri));
		
		matcher.find();
		for(int i = 1; i <= matcher.groupCount(); i++) {
			groups.add(matcher.group(i));
		}
		
		return groups;
	}
	
}
