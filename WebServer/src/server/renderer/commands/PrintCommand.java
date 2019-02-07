package server.renderer.commands;

import java.io.IOException;
import java.util.LinkedList;

import server.renderer.InterpreterException;
import server.renderer.Renderer;

public class PrintCommand extends Command {

	@Override
	public String run(StringBuilder code, LinkedList <String> languages) throws IOException, InterpreterException {
		return Renderer.next(code);
	}
	
}
