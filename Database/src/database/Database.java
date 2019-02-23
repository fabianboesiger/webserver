package database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;

import database.templates.Identifiable;
import database.templates.ObjectTemplate;

public class Database {
	
	private static final Charset ENCODING = StandardCharsets.UTF_8;
	private static final String METADATA_FILE_NAME = ".metadata.txt";
	private static final File DATA_FOLDER = new File("data");
	private static final String ENDING = "txt";
	
	public Database() {
		System.out.println("Starting database with directory " + DATA_FOLDER.getAbsolutePath());
		DATA_FOLDER.mkdirs();
	}
	
	public synchronized ObjectTemplate load(String name, String id) {
		try {
			File file = getFile(name, encrypt(id));
			if(!file.exists()) {
				return null;
			}
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), ENCODING));
			String line = null;
			ArrayList <String> keys = new ArrayList <String> ();
			while((line = bufferedReader.readLine()) != null) {
				keys.add(line);
			}
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		return null;
	}
	
	public synchronized boolean save(ObjectTemplate objectTemplate, boolean overwrite) {
		try {
			String id = objectTemplate.getIdentifier().getId();
			if(id == null) {
				id = Integer.toHexString(getCount(objectTemplate.getName()));
			} else {
				id = encrypt(id);
			}
			File file = getFile(objectTemplate.getName(), id);
			
			if(!overwrite && file.exists()) {
				return false;
			}
			
			objectTemplate.get();
		
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), ENCODING));
			LinkedList <String> lines = objectTemplate.render();
			for(int i = 0; i < lines.size(); i++) {
				bufferedWriter.write(lines.get(i));
				if(i != lines.size() - 1) {
					bufferedWriter.newLine();
				}
			}
			bufferedWriter.close();
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private int getCount(String name) {
		File folder = new File(DATA_FOLDER.getName() + "/" + name);
		folder.mkdirs();
		return folder.listFiles().length;
	}
	
	private File getFile(String name, String id) {
		File file = new File(DATA_FOLDER.getName() + "/" + name + "/" + id + "." + ENDING);
		file.getParentFile().mkdirs();
		return file;
	}
	
	public static String encrypt(String input) {
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
	
	/*
	public static String decrypt(String input) {
		StringBuilder output = new StringBuilder();
		for(int i = 0; i < input.length(); i += 2) {
			output.append((char) Integer.parseInt("" + input.charAt(i) + input.charAt(i + 1), 16));
		}
		return output.toString();
	}
	*/
	
}
