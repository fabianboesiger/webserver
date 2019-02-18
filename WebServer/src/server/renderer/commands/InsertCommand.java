package server.renderer.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;

import server.renderer.InterpreterException;
import server.renderer.Renderer;
import server.renderer.container.Container;
import server.renderer.container.ObjectContainer;

public class InsertCommand extends Command {

	@Override
	public Container run(StringBuilder code, LinkedList <String> languages, ObjectContainer variables, StringBuilder printer, BufferedReader insert) throws IOException, InterpreterException {
		printer.append(Renderer.render(insert, languages, variables));
		return null;
	}
	
}
