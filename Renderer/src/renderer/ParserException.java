package renderer;

public class ParserException extends InterpreterException {

	private static final long serialVersionUID = -1338977958492469707L;

	public ParserException(String message) {
		super("Parser Exception: " + message);
	}

}
