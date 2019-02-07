package server.renderer;

import java.io.IOException;
import java.util.LinkedList;

import application.Application;

public class SetCommand extends Command {

	private Application application;

	public SetCommand(Application application) {
		this.application = application;
	}

	@Override
	protected String run(StringBuilder code, LinkedList <String> languages) throws IOException, InterpreterException {
		return null;
	}
	
}
