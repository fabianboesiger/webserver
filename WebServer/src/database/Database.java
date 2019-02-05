package database;

import java.io.File;

import database.structures.DataObject;

public class Database {
	
	private static final File DATA_FOLDER = new File("data");
	
	public Database() {
		
	}
	
	public void set(String key, DataObject dataObject) {
		
	}
	
	public DataObject get() {
		return null;
	}
	
	private static String encrypt(String input) {
		StringBuilder output = new StringBuilder();
		for(int i = 0; i < input.length(); i++) {
			int number = input.charAt(i);
			StringBuilder hex = new StringBuilder(Integer.toHexString(number));
			while(hex.length() < 2) {
				hex.insert(0, "0");
			}
			output.append(hex);
		}
		return output.toString();
	}
	
	private static String decrypt(String input) {
		StringBuilder output = new StringBuilder();
		for(int i = 0; i < input.length(); i += 2) {
			output.append((char) Integer.parseInt("" + input.charAt(i) + input.charAt(i + 1), 16));
		}
		return output.toString();
	}
	
}
