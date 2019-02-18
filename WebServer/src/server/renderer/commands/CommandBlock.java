package server.renderer.commands;

import server.renderer.Renderer;

public abstract class CommandBlock extends Command {
	
	protected static final String END = "end";
	
	protected void skip(StringBuilder code) {
		int count = 1;
		while(count != 0) {
			String next = Renderer.nextCommand(code).toLowerCase();
			if(next.equals(END)) {
				count--;
			} else
			if(Renderer.getCommand(next) instanceof CommandBlock) {
				count++;
			}
		}
	}
	
}
