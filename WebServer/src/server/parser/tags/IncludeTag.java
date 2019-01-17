package server.parser.tags;

import java.util.HashMap;

import server.Loader;

public class IncludeTag extends Tag {

	@Override
	public String parse(HashMap <String, String> attributes) {
		Loader loader = new Loader(attributes.get("src"));
		loader.load();
		
		return loader.buildString(loader.getInputStream()).toString();
	}

}
