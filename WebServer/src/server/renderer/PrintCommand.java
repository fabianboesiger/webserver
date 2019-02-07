package server.renderer;

import java.io.IOException;
import java.util.LinkedList;

public class PrintCommand extends Command {

	@Override
	protected String run(StringBuilder code, LinkedList <String> languages) throws IOException, InterpreterException {
		return Renderer.next(code);
	}
	
}
