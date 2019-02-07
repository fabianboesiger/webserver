package server.renderer.commands;

import server.renderer.InterpreterException;

public class UnknownCommandException extends InterpreterException {

	private static final long serialVersionUID = -7685378123332959223L;

	public UnknownCommandException(String message) {
		super("Unknown Command Exception: " + message);
	}
	
}
