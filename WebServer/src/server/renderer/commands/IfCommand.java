package server.renderer.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;

import server.renderer.InterpreterException;
import server.renderer.Renderer;
import server.renderer.container.Container;
import server.renderer.container.ObjectContainer;

public class IfCommand extends CommandBlock {
		
	@Override
	public Container run(StringBuilder code, LinkedList <String> languages, ObjectContainer variables, StringBuilder printer, BufferedReader insert) throws IOException, InterpreterException {
		if(Renderer.nextBoolean(code, languages, variables, printer, insert)) {
			String next;
			while(!(next = Renderer.nextCommand(code)).toLowerCase().equals(END)) {
				Renderer.run(next, code, languages, variables, printer, insert);
			}
		} else {
			skip(code);
		}
		return null;
	}
	
}