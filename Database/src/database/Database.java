package database;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import database.templates.ObjectTemplate;

public class Database {
	
	public static final Charset ENCODING = StandardCharsets.UTF_8;
	private static final File DATA_FOLDER = new File("data");
	private static final String ENDING = "txt";
	
	public Database() {
		System.out.println("Starting database with directory " + DATA_FOLDER.getAbsolutePath());
		DATA_FOLDER.mkdirs();
	}
	
	public synchronized boolean deleteId(Class <?> target, String id) {
		if(id != null) {
			try {
				File file = getFile(target.getSimpleName(), id);
				return file.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public synchronized ObjectTemplate loadId(Class <?> target, String id) {
		if(id != null) {
			HashMap <String, ObjectTemplate> initialized = new HashMap <String, ObjectTemplate> ();
			try {
				File file = getFile(target.getSimpleName(), id);
				if(file == null || !file.exists()) {
					return null;
				}
				ObjectTemplate objectTemplate = (ObjectTemplate) target.getConstructor().newInstance();
				objectTemplate.parse(this, id, initialized);
				return objectTemplate;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public synchronized ObjectTemplate[] loadAll(Class <?> target) {
		try {
			File folder = new File(DATA_FOLDER.getPath() + File.separator + target.getField("NAME").get(null));
			folder.getParentFile().mkdirs();
			File[] files = folder.listFiles();
			ObjectTemplate[] output = new ObjectTemplate[files.length];
			for(int i = 0; i < files.length; i++) {
				String name = files[i].getPath();
				if((output[i] = loadId(target, name.substring(0, name.lastIndexOf(".")))) == null) {
					return null;
				}
			}
			return output;
		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public synchronized ObjectTemplate load(Class <?> target, String id) {
		if(id != null) {
			return loadId(target, encrypt(id));
		}
		return null;
	}
	
	public synchronized boolean delete(Class <?> target, String id) {
		if(id != null) {
			return deleteId(target, encrypt(id));
		}
		return false;
	}

	/*
	public synchronized boolean load(ObjectTemplate objectTemplate, int id) {
		if(id < getCount(objectTemplate.getClass().getSimpleName())) {
			return loadId(objectTemplate, Integer.toHexString(id));
		}
		return false;
	}
	*/
	
	public synchronized boolean save(ObjectTemplate objectTemplate) {
		if(objectTemplate.check(this, false)) {
			try {
				objectTemplate.render(this);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public synchronized boolean update(ObjectTemplate objectTemplate) {
		if(objectTemplate.check(this, true)) {
			try {
				objectTemplate.render(this);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public int getCount(String name) {
		File folder = new File(DATA_FOLDER.getPath() + File.separator + name);
		folder.mkdirs();
		return folder.listFiles().length;
	}
	
	public File getFile(String name, String id) {
		File file = new File(DATA_FOLDER.getPath() + File.separator + name + File.separator + id + "." + ENDING);
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
