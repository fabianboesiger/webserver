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
		return uri.toString().matches(path+"(\\?.*)?(#.*)?");
	}
	
	public ArrayList <String> getGroups(URI uri) {
		ArrayList <String> groups = new ArrayList <String> ();
		Pattern pattern = Pattern.compile(path);
		Matcher matcher = pattern.matcher(uri.toString());
		
		matcher.find();
		for(int i = 1; i <= matcher.groupCount(); i++) {
			groups.add(matcher.group(i));
		}
		
		return groups;
	}
	
}
