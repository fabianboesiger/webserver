package database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;

import database.templates.ObjectTemplate;

public class Database {
	
	private static final Charset ENCODING = StandardCharsets.UTF_8;
	private static final File DATA_FOLDER = new File("data");
	private static final String ENDING = "txt";
	
	public Database() {
		System.out.println("Starting database with directory " + DATA_FOLDER.getAbsolutePath());
		DATA_FOLDER.mkdirs();
	}
	
	public synchronized boolean load(ObjectTemplate objectTemplate, String id) {
		if(id != null) {
			try {
				File file = getFile(objectTemplate.getName(), encrypt(id));
				if(file.exists()) {	
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), ENCODING));
					HashMap <String, String> map = new HashMap <String, String> ();
					
					String line = null;
					while((line = bufferedReader.readLine()) != null) {
						int indexOfEquals = line.indexOf("=");
						if(indexOfEquals != -1) {
							String key = line.substring(0, indexOfEquals).trim();
							String value = line.substring(indexOfEquals + 1, line.length());
							map.put(key, value);
						}
					}
					bufferedReader.close();
					
					objectTemplate.parse(map);
					
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}  
		}
		return false;
	}
	
	public synchronized boolean load(ObjectTemplate objectTemplate, int id) {
		if(id < getCount(objectTemplate.getName())) {
			try {
				File file = getFile(objectTemplate.getName(), Integer.toHexString(id));
				if(file.exists()) {	
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), ENCODING));
					HashMap <String, String> map = new HashMap <String, String> ();
					
					String line = null;
					while((line = bufferedReader.readLine()) != null) {
						int indexOfEquals = line.indexOf("=");
						if(indexOfEquals != -1) {
							String key = line.substring(0, indexOfEquals).trim();
							String value = line.substring(indexOfEquals + 1, line.length());
							map.put(key, value);
						}
					}
					bufferedReader.close();
					
					objectTemplate.parse(map);
					
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}  
		}
		return false;
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
	
	public int getCount(String name) {
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
