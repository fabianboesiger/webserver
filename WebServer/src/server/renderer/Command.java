package server.renderer;

import java.io.IOException;

public abstract class Command {
	
	protected abstract String run(StringBuilder code) throws IOException, InterpreterException;
	
}
