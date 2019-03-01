package database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
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
	
	public synchronized boolean loadId(ObjectTemplate objectTemplate, String id) {
		if(id != null) {
			return loadId(objectTemplate, getFile(objectTemplate.NAME, id));		 
		}
		return false;
	}
	

	public synchronized boolean loadId(ObjectTemplate objectTemplate, File file) {
		if(file != null) {
			try {
				if(file.exists()) {	
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), ENCODING));
					HashMap <String, String> map = new HashMap <String, String> ();
					
					String line = null;
					while((line = bufferedReader.readLine()) != null) {
						int indexOfEquals = line.indexOf("=");
						if(indexOfEquals != -1) {
							String key = line.substring(0, indexOfEquals).trim();
							String value = line.substring(indexOfEquals + 1, line.length()).trim();
							map.put(key, value);
						}
					}
					bufferedReader.close();
					
					objectTemplate.parse(this, map);
					
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}  
		}
		return false;
	}
	
	public synchronized ObjectTemplate[] loadAll(Class <?> target) {
		try {
			File folder = new File(DATA_FOLDER.getName() + "/" + target.getField("NAME").get(null));
			folder.getParentFile().mkdirs();
			File[] files = folder.listFiles();
			ObjectTemplate[] output = new ObjectTemplate[files.length];
			for(int i = 0; i < files.length; i++) {
				output[i] = (ObjectTemplate) target.getConstructor().newInstance();
				loadId(output[i], files[i]);
			}
			return output;
		} catch (NoSuchFieldException | InstantiationException | IllegalArgumentException | IllegalAccessException | SecurityException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public synchronized boolean load(ObjectTemplate objectTemplate, String id) {
		if(id != null) {
			return loadId(objectTemplate, encrypt(id));
		}
		return false;
	}
	
	public synchronized boolean load(ObjectTemplate objectTemplate, int id) {
		if(id < getCount(objectTemplate.NAME)) {
			return loadId(objectTemplate, Integer.toHexString(id));
		}
		return false;
	}
	
	public synchronized String save(ObjectTemplate objectTemplate) {
		try {
			String id = objectTemplate.getIdentifier().getId();
			if(id == null) {
				id = Integer.toHexString(getCount(objectTemplate.NAME));
			} else {
				id = encrypt(id);
			}
			File file = getFile(objectTemplate.NAME, id);
			
			if(file.exists()) {
				return null;
			}
			
			objectTemplate.get();
		
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), ENCODING));
			LinkedList <String> lines = objectTemplate.render(this);
			for(int i = 0; i < lines.size(); i++) {
				bufferedWriter.write(lines.get(i));
				if(i != lines.size() - 1) {
					bufferedWriter.newLine();
				}
			}
			bufferedWriter.close();
			
			return id;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
