package server.renderer.commands;

import java.io.IOException;
import java.util.LinkedList;

import application.Application;
import server.renderer.InterpreterException;

public class SetCommand extends Command {

	private Application application;

	public SetCommand(Application application) {
		this.application = application;
	}

	@Override
	public String run(StringBuilder code, LinkedList <String> languages) throws IOException, InterpreterException {
		return null;
	}
	
}
