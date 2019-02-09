package server.renderer.commands;

import java.io.IOException;
import java.util.LinkedList;

import server.renderer.InterpreterException;
import server.renderer.Renderer;
import server.renderer.container.Container;
import server.renderer.container.ObjectContainer;

public class PrintCommand extends Command {
	
	@Override
	public Container run(StringBuilder code, LinkedList <String> languages, ObjectContainer container, StringBuilder printer) throws IOException, InterpreterException {
		printer.append(Renderer.runNext(code, languages, container, printer).toString());
		return null;
	}
	
}
