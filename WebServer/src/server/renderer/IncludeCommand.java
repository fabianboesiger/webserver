package server.renderer;

import java.io.File;
import java.io.IOException;

import server.Response;

public class IncludeCommand extends Command {

	public IncludeCommand() {
		
	}

	@Override
	protected String run(StringBuilder code) throws IOException, InterpreterException {
		return Renderer.render(new File(Response.VIEWS_FOLDER.getName() + "/" + Renderer.next(code).toString()));
	}
	
}
