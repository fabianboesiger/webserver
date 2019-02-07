package server.renderer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

import application.Application;
import server.renderer.commands.Command;
import server.renderer.commands.GetCommand;
import server.renderer.commands.IncludeCommand;
import server.renderer.commands.PrintCommand;
import server.renderer.commands.SetCommand;
import server.renderer.commands.TranslateCommand;
import server.renderer.commands.UnknownCommandException;
import server.renderer.container.ObjectContainer;

public class Renderer {
	
	private static final String KEYWORD = "server";
	private static final String BEGIN = "<" + KEYWORD + ">";
	private static final String END = "</" + KEYWORD + ">";
	private static final char STRING = '"';
	private static final char ESCAPE = '\\';

	private HashMap <String, Command> commands;
	private ObjectContainer variables;
	
	public Renderer(Application application, ObjectContainer predefined) {
		variables = new ObjectContainer();
		variables.putAll(predefined);
		
		commands = new HashMap <String, Command> ();
		commands.put("print", new PrintCommand());
		commands.put("set", new SetCommand(application));
		commands.put("get", new GetCommand(application));
		commands.put("include", new IncludeCommand(this));
		commands.put("translate", new TranslateCommand(this));
	}
	
	public String render(File file, LinkedList <String> languages) throws IOException, InterpreterException {
		return render(new BufferedReader(new InputStreamReader(new FileInputStream(file))), languages);
	}
	
	public String render(BufferedReader bufferedReader, LinkedList <String> languages) throws IOException, InterpreterException {
		
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
        			output.append(interpret(code, languages));
        		}
        	}
        }
        bufferedReader.close();
        
        return output.toString();
	}
	
	private StringBuilder interpret(StringBuilder code, LinkedList <String> languages) throws InterpreterException, IOException {
		StringBuilder output = new StringBuilder();
		while(code.length() > 0) {
			String command = next(code);
			if(command != null) {
				String result = run(command.toLowerCase(), code, languages);
				if(result != null) {
					output.append(result);
				}
			}
		}
		return output;
	}
	
	public static String next(StringBuilder code) {
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

		return null;
	}
	
	private String run(String command, StringBuilder code, LinkedList <String> languages) throws InterpreterException, IOException {
		if(commands.containsKey(command)) {
			return commands.get(command).run(code, languages);
		}
		throw new UnknownCommandException(command);
	}
	
}