package server.parser;

import java.util.HashMap;

public abstract class Parser {
	
	private static final char TAG_START = '<';
	private static final char TAG_END = '>';
	private static final char STRING_START = '"';
	private static final char STRING_END = '"';
	
	public static String parse(StringBuilder input) {
		return parseTag(input);
	};
	
	private static String parseTag(StringBuilder input) {
		String output = "";
		
		while(input.length() > 0) {
			char current = input.charAt(0);
			input.deleteCharAt(1);
			if(current == TAG_START) {
				output += TAG_START;
				output += parseAttributes(input);
				output += parseTag(input);
			} else {
				output += current;
			}
		}
		
		return output;
	}
	
	private static String parseAttributes(StringBuilder input) {
		String output = "";
		
		while(input.length() > 0) {
			char current = input.charAt(0);
			input.deleteCharAt(1);
			if(current == TAG_END) {
				output += TAG_END;
			} else {
				output += parseAttribute(input);
			}
		}
		
		return output;
	}
	
	private static String parseAttribute(StringBuilder input) {
		String output = "";
		
		return output;
	}
	
}