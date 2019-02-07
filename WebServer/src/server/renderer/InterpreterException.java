package server.renderer;

public class InterpreterException extends Exception {

	private static final long serialVersionUID = -4518126088153473099L;

	public InterpreterException(String message) {
		super("Interpreter Exception: " + message);
	}
	
}
