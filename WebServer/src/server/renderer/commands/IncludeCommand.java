package server.renderer.commands;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import server.Responder;
import server.renderer.InterpreterException;
import server.renderer.Renderer;
import server.renderer.container.Container;

public class IncludeCommand extends Command {

	@Override
	public Container run(StringBuilder code, LinkedList <String> languages, Container container, StringBuilder printer) throws IOException, InterpreterException {
		printer.append(Renderer.render(new File(Responder.VIEWS_FOLDER.getName() + "/" + Renderer.nextString(code)), languages, container));
		return null;
	}
	
}
