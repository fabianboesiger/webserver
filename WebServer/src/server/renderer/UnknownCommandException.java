package server.renderer;

public class UnknownCommandException extends InterpreterException {

	private static final long serialVersionUID = -7685378123332959223L;

	UnknownCommandException(String message) {
		super("Unknown Command Exception: " + message);
	}
	
}
