package database;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Predicate;

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
				File file = getFile(target, id);
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
				File file = getFile(target, id);
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
	
	public synchronized LinkedList <ObjectTemplate> loadAll(Class <?> target, Predicate <ObjectTemplate> predicate) {
		return loadAll(target, 0, null, predicate);
	}
	
	public synchronized LinkedList <ObjectTemplate> loadAll(Class <?> target) {
		return loadAll(target, 0, null, null);
	}
	
	public synchronized LinkedList <ObjectTemplate> loadAll(Class <?> target, Integer from) {
		return loadAll(target, from, null, null);
	}
	
	public synchronized LinkedList <ObjectTemplate> loadAll(Class <?> target, Integer from, Predicate <ObjectTemplate> predicate) {
		return loadAll(target, from, null, predicate);
	}
	
	public synchronized LinkedList <ObjectTemplate> loadAll(Class <?> target, Integer from, Integer range) {
		return loadAll(target, from, range, null);
	}
	
	public synchronized LinkedList <ObjectTemplate> loadAll(Class <?> target, Integer from, Integer range, Predicate <ObjectTemplate> predicate) {
		try {
			File folder = new File(DATA_FOLDER.getPath() + File.separator + target.getSimpleName());
			folder.getParentFile().mkdirs();
			File[] files = folder.listFiles();
			if(files != null) {
				Arrays.sort(files);
				LinkedList <ObjectTemplate> output = new LinkedList <ObjectTemplate> ();
				int ignoreCount = 0;
				for(int i = 0; i < files.length; i++) {
					String fullName = files[i].getName();
					String name = fullName.substring(0, fullName.lastIndexOf("."));
					ObjectTemplate element = null; 
					if((element = loadId(target, name)) == null) {
						return null;
					} else {
						if(predicate != null && !predicate.test(element)) {
							ignoreCount++;
						} else {
							if(i - ignoreCount >= from) {
								output.add(element);
								if(range != null && output.size() == range) {
									break;
								}
							}
						}
					}
				}
				return output;
			}
		} catch (IllegalArgumentException | SecurityException e) {
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
	
	public int getCount(Class <?> target) {
		File folder = new File(DATA_FOLDER.getPath() + File.separator + target.getSimpleName());
		return folder.listFiles().length;
	}
	
	public int getNext(Class <?> target) {
		File folder = new File(DATA_FOLDER.getPath() + File.separator + target.getSimpleName());
		folder.getParentFile().mkdirs();
		File[] files = folder.listFiles();System.out.println(files);
		if(files == null || files.length == 0) {
			return 0;
		}
		Arrays.sort(files);
		String name = files[files.length - 1].getName();
		return Integer.valueOf(name.substring(0, name.lastIndexOf(".")), 16).intValue() + 1;
	}
	
	public File getFile(Class <?> target, String id) {
		File file = new File(DATA_FOLDER.getPath() + File.separator + target.getSimpleName() + File.separator + id + "." + ENDING);
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
