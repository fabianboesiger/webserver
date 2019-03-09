package renderer.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import renderer.InterpreterException;
import renderer.Renderer;

public class HTMLEncodeCommand extends Command {
	
	@Override
	public Object run(StringBuilder code, List <String> languages, Map <String, Object> variables, StringBuilder printer, File folder) throws IOException, InterpreterException {
		String input = (String) Renderer.next(code, languages, variables, printer, folder);
		StringBuilder output = new StringBuilder();
		for(int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			switch(c) {
			case '<':
				output.append("&lt;");
				break;
			case '>':
				output.append("&gt;");
				break;
			case '&':
				output.append("&amp;");
				break;
			default:
				output.append(c);
				break;
			}
		}
		return output.toString();
	}
	
}

