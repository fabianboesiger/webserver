package server.renderer.commands;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

import server.Responder;
import server.renderer.InterpreterException;
import server.renderer.Renderer;

public class IncludeCommand extends Command {

	@Override
	public Object run(StringBuilder code, LinkedList <String> languages, Map <String, Object> variables, StringBuilder printer) throws IOException, InterpreterException {
		printer.append(Renderer.render(new File(Responder.VIEWS_FOLDER.getName() + "/" + Renderer.next(code, languages, variables, printer)), languages, variables));
		return null;
	}
	
}
