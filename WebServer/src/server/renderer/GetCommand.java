package server.renderer;

import java.io.IOException;
import java.util.LinkedList;

import application.Application;

public class GetCommand extends Command {
	
	private Application application;
	
	public GetCommand(Application application) {
		this.application = application;
	}

	@Override
	protected String run(StringBuilder code, LinkedList <String> languages) throws IOException, InterpreterException {
		return null;
	}
	
}
