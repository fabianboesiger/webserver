package renderer.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import renderer.InterpreterException;
import renderer.Renderer;

public class MarkdownCommand extends Command {
	
	@Override
	public Object run(StringBuilder code, List <String> languages, Map <String, Object> variables, StringBuilder printer, File folder) throws IOException, InterpreterException {
		String input = (String) Renderer.next(code, languages, variables, printer, folder);
		StringBuilder output = new StringBuilder();
		
		String[] splitted = input.split("(\\r\\n|\\r|\\n)");
		
		boolean list = true;
		
		for(String line : splitted) {
			String trimmed = line.trim();
			
			if(trimmed.startsWith("*")) {
				if(!list) {
					list = true;
					output.append("<ul>");
				}
				output.append("<li>");
				output.append(trimmed.substring(1).trim());
				output.append("</li>");
			} else {
				if(list) {
					list = false;
					output.append("</ul>");
				}
				boolean heading = false;
				for(int i = 6; i > 0; i--) {
					StringBuilder start = new StringBuilder();
					for(int j = 0; j < i; j++) {
						start.append("#");
					}
					if(trimmed.startsWith(start.toString())) {
						output.append("<h" + i + ">");
						output.append(trimmed.substring(start.length()).trim());
						output.append("</h" + i + ">");
						heading = true;
						break;
					}
				}
				if(!heading) {
					output.append("<p>");
					output.append(trimmed);
					output.append("</p>");
				}
			}
		}
		
		if(list) {
			list = false;
			output.append("</ul>");
		}
				
		return output.toString();
	}
	
}

