package renderer.commands;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import renderer.InterpreterException;
import renderer.Renderer;

public class URLEncodeCommand extends Command {
	
	@Override
	public Object run(StringBuilder code, List <String> languages, Map <String, Object> variables, StringBuilder printer, File folder) throws IOException, InterpreterException {
		String input = (String) Renderer.next(code, languages, variables, printer, folder);
		return URLEncoder.encode(input, Renderer.ENCODING.toString());
	}
	
}

