package server.handlers.parser.tags;

import java.io.IOException;
import java.util.HashMap;

public abstract class Tag {
	
	public abstract String parse(HashMap <String, String> attributes) throws IOException;
	
}
