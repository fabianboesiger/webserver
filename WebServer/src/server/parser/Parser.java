package server.parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import server.parser.tags.IncludeTag;
import server.parser.tags.Tag;

public abstract class Parser {
	
	private static final char TAG_START = '<';
	private static final char TAG_END = '>';
	private static final char EQUALS = '=';
	private static final char STRING = '"';
	
	private static HashMap <String, Tag> tags;
	
	static {
		tags = new HashMap <String, Tag> ();
		tags.put("include", new IncludeTag());
	}
	
	public static String parse(StringBuilder input) {
		return parseTag(input);
	};
	
	private static String parseTag(StringBuilder input) {
		String output = "";
				
		while(input.length() > 0) {
			char current = input.charAt(0);
			input.deleteCharAt(0);
			
			if(current == TAG_START) {
				output += TAG_START;
				output += parseAttributes(input);
				output += TAG_END;
				output += parseTag(input);
			} else {
				output += current;
			}
		}
				
		return output;
	}
	
	private static String parseAttributes(StringBuilder input) {
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
				
		output += name.toLowerCase();
		if(tags.containsKey(name)) {
			tags.get(name).parse(attributes);
		} else {
			Iterator <Entry <String, String>> iterator = attributes.entrySet().iterator();
			while(iterator.hasNext()) {
				Entry <String, String> entry = iterator.next();
				output += (" " + entry.getKey());
				value = entry.getValue();
				if(value != null) {
					output += ("" + EQUALS + STRING + value + STRING);
				}
			}
		}
				
		return output;
	}
	
	
}