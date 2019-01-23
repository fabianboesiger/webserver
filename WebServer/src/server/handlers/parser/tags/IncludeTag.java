package server.handlers.parser.tags;

import java.io.IOException;
import java.util.HashMap;

import server.handlers.parser.Parser;

public class IncludeTag extends Tag {

	@Override
	public String parse(HashMap <String, String> attributes) throws IOException {
		Parser parser = new Parser(attributes.get("src"));
		return parser.parse();
	}

}
