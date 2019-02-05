package server.renderer;

import java.io.IOException;

public class PrintCommand extends Command {

	public PrintCommand() {
		
	}

	@Override
	protected String run(StringBuilder code) throws IOException, InterpreterException {
		return Renderer.next(code);
	}
	
}
