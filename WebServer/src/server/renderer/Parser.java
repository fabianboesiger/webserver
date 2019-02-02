package server.renderer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import server.renderer.structure.Tag;

public class Parser {
	
	private static final char TAG_START = '<';
	private static final char TAG_END = '>';
	private static final char EQUALS = '=';
	private static final char STRING = '"';
	private static final char END_CHAR = '/';
	private static final String ENCODING = "UTF-8";

	private File file;
	
	public Parser(File file) {
		this.file = file;
	}
	
	public Parser(String path) {
		this(new File(path));
	}
	
	
	/*
	public String render() throws IOException {
		return parse(toStringBuilder(new FileInputStream(file))).render();
	}
	
	private static StringBuilder toStringBuilder(InputStream inputStream) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, ENCODING));
	    	StringBuilder stringBuilder = new StringBuilder();
	        String line;
	        while((line = bufferedReader.readLine()) != null) {
	        	stringBuilder.append(line);
	        	stringBuilder.append("\n");
	        }
	        bufferedReader.close();
	        return stringBuilder;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Tag parse(StringBuilder input) throws IOException {
		parseTag(input);
		return null;
	}
	
	private static String parseTag(StringBuilder input) throws IOException {
		String output = "";
		
		boolean insideTag = false;
				
		while(input.length() > 0) {
			char current = input.charAt(0);
			input.deleteCharAt(0);
			
			if(!insideTag) {
				if(current == TAG_START) {
					output += TAG_START;
					output += parseAttributes(input);
					output += TAG_END;
					output += parseTag(input);
				} else {
					output += current;
				}
			}
		}
				
		return output;
	}
	
	private static HashMap <String, String> parseAttributes(StringBuilder input) throws IOException {
		String output = "";
		
		String name = "";
		HashMap <String, String> attributes = new HashMap <String, String> ();
		
		boolean parseKey = true;
		boolean equals = false;
		boolean parseValue = false;
		String key = "";
		String value = "";
		
		while(input.length() > 0) {
			char current = input.charAt(0);
			input.deleteCharAt(0);
						
			if(!equals) {
				if(current == TAG_END) {
					if(name.length() == 0) {
						name = key;
					} else {
						attributes.put(key, null);
					}
					break;
				} else 
				if(current == EQUALS) {
					parseKey = false;
					equals = true;
				} else
				if(Character.isWhitespace(current)) {
					if(parseKey && key.length() > 0) {
						parseKey = false;
					}
				} else {
					if(!parseKey) {
						parseKey = true;
						if(name.length() == 0) {
							name = key;
						} else {
							attributes.put(key, null);
						}
						key = "";
					}
					if(parseKey) {
						key += current;
					}
				}
			} else {
				if(parseValue) {
					if(current == STRING) {
						parseValue = false;
						parseKey = true;
						equals = false;
						attributes.put(key, value);
						key = "";
						value = "";
					} else {
						value += current;
					}
				} else {
					if(current == STRING) {
						parseValue = true;
					}
				}
			}
		}
			
		return output;
	}
	*/
	
}