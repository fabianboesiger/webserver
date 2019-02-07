package server.renderer;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import server.Responder;

public class IncludeCommand extends Command {
	
	private Renderer renderer;
	
	public IncludeCommand(Renderer renderer) {
		this.renderer = renderer;
	}

	@Override
	protected String run(StringBuilder code, LinkedList <String> languages) throws IOException, InterpreterException {
		return renderer.render(new File(Responder.VIEWS_FOLDER.getName() + "/" + Renderer.next(code).toString()), languages);
	}
	
}
