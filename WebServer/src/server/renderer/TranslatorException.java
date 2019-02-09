package server.renderer;

public class TranslatorException extends InterpreterException {

	private static final long serialVersionUID = -5833734627444405047L;

	public TranslatorException(String message) {
		super("Translator Exception: " + message);
	}

}
