package server.loaders.parser.tags;

import java.util.HashMap;

import server.loaders.Loader;
import server.loaders.ViewsLoader;

public class IncludeTag extends Tag {

	@Override
	public String parse(HashMap <String, String> attributes) {
		Loader loader = new ViewsLoader(attributes.get("src"));
		loader.load();
		
		return loader.buildString(loader.getInputStream()).toString();
	}

}
