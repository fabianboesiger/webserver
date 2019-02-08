package server.renderer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

import server.renderer.commands.AllCommand;
import server.renderer.commands.Command;
import server.renderer.commands.GetCommand;
import server.renderer.commands.IncludeCommand;
import server.renderer.commands.PrintCommand;
import server.renderer.commands.SetCommand;
import server.renderer.commands.TranslateCommand;
import server.renderer.commands.UnknownCommandException;
import server.renderer.container.Container;

public class Renderer {
	
	private static final String KEYWORD = "server";
	private static final String BEGIN = "<" + KEYWORD + ">";
	private static final String END = "</" + KEYWORD + ">";
	private static final char STRING = '"';
	private static final char ESCAPE = '\\';

	private static HashMap <String, Command> commands;
		
	static {
		commands = new HashMap <String, Command> ();
		commands.put("print", new PrintCommand());
		commands.put("set", new SetCommand());
		commands.put("get", new GetCommand());
		commands.put("include", new IncludeCommand());
		commands.put("translate", new TranslateCommand());
		commands.put("all", new AllCommand());
	}
	
	public static String render(File file, LinkedList <String> languages, Container variables) throws IOException, InterpreterException {
		return render(new BufferedReader(new InputStreamReader(new FileInputStream(file))), languages, variables);
	}
	
	public static String render(BufferedReader bufferedReader, LinkedList <String> languages, Container variables) throws IOException, InterpreterException {
		
		StringBuilder buffer = new StringBuilder();
		StringBuilder code = new StringBuilder();
    	StringBuilder output = new StringBuilder();
		boolean insideTag = false;

		int next;
        while((next = bufferedReader.read()) != -1) {
        	char character = (char) next;
    		buffer.append(character);
        	if(!insideTag) {
        		if(character != BEGIN.charAt(buffer.length()-1)) {
        			output.append(buffer);
        			buffer.setLength(0);
        		} else
        		if(buffer.length() >= BEGIN.length()) {
        			insideTag = true;
        			buffer.setLength(0);
        		}
        	} else {
        		if(character != END.charAt(buffer.length()-1)) {
        			code.append(buffer);
        			buffer.setLength(0);
        		} else
        		if(buffer.length() >= END.length()) {
        			insideTag = false;
        			buffer.setLength(0);
        			output.append(interpret(code, languages, variables));
        		}
        	}
        }
        bufferedReader.close();
        
        return output.toString();
	}
	
	private static StringBuilder interpret(StringBuilder code, LinkedList <String> languages, Container variables) throws InterpreterException, IOException {
		StringBuilder printer = new StringBuilder();
		while(code.length() > 0) {
			String command = nextString(code);
			if(command != null) {
				run(command.toLowerCase(), code, languages, variables, printer);
			}
		}
		return printer;
	}
	
	public static String nextString(StringBuilder code) throws MalformedCommandException {
		StringBuilder buffer = new StringBuilder();
		boolean insideString = false;
		boolean escaped = false;
						
		while(code.length() > 0) {
			char character = code.charAt(0);
			code.deleteCharAt(0);
			if(!insideString) {
				if(character == STRING) {
					insideString = true;
				} else
				if(Character.isWhitespace(character)) {
					if(buffer.length() > 0) {
						return buffer.toString();
					}
				} else {
					buffer.append(character);
				}
			} else {
				if(escaped) {
					if(character == STRING) {
						buffer.append(STRING);
					} else
					if(character == ESCAPE) {
						buffer.append(ESCAPE);
					}
				} else {
					if(character == ESCAPE) {
						escaped = true;
					} else
					if(character == STRING) {
						if(buffer.length() > 0) {
							return buffer.toString();
						}
					} else {
						buffer.append(character);
					}
				}
			}
		}
		if(buffer.length() > 0) {
			return buffer.toString();
		}

		throw new MalformedCommandException();
	}
	
	public static Container run(String command, StringBuilder code, LinkedList <String> languages, Container container, StringBuilder printer) throws InterpreterException, IOException {
		if(commands.containsKey(command)) {
			return commands.get(command).run(code, languages, container, printer);
		}
		throw new UnknownCommandException(command);
	}
	
	public static int parseInt(String string) throws ParserException {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			throw new ParserException(string);
		}
	}
	
	public static int nextInt(StringBuilder code) throws ParserException, MalformedCommandException {
		return parseInt(nextString(code));
	}
	
	public static double parseDouble(String string) throws ParserException {
		try {
			return Double.parseDouble(string);
		} catch (NumberFormatException e) {
			throw new ParserException(string);
		}
	}
	
	public static double nextDouble(StringBuilder code) throws ParserException, MalformedCommandException {
		return parseDouble(nextString(code));
	}
	
}