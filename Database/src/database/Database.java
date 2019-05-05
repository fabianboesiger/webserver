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
	public static final int COUNTER_LENGTH = 32;
	
	public Database() {
		System.out.println("Starting Database with Directory " + DATA_FOLDER.getAbsolutePath());
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
	
	public synchronized ObjectTemplate loadId(Class <?> target, String id, Object caller) {
		if(id != null) {
			HashMap <String, ObjectTemplate> initialized = new HashMap <String, ObjectTemplate> ();
			try {
				File file = getFile(target, id);
				if(file == null || !file.exists()) {
					return null;
				}
				ObjectTemplate objectTemplate = null;
				if(caller != null) {
					objectTemplate = (ObjectTemplate) target.getConstructor(caller.getClass()).newInstance(caller);
				} else {
					objectTemplate = (ObjectTemplate) target.getConstructor().newInstance();
				}
				objectTemplate.parse(this, id, initialized);
				objectTemplate.resetLoad();
				return objectTemplate;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	

	public synchronized ObjectTemplate loadId(Class <?> target, String id) {
		return loadId(target, id, null);
	}
	
	public synchronized LinkedList <ObjectTemplate> loadAll(Class <?> target, Predicate <ObjectTemplate> predicate) {
		return loadAll(target, 0, null, predicate, null);
	}
	
	public synchronized LinkedList <ObjectTemplate> loadAll(Class <?> target, Predicate <ObjectTemplate> predicate, Object caller) {
		return loadAll(target, 0, null, predicate, caller);
	}
	
	public synchronized LinkedList <ObjectTemplate> loadAll(Class <?> target) {
		return loadAll(target, 0, null, null);
	}
	
	public synchronized LinkedList <ObjectTemplate> loadAll(Class <?> target, Integer from) {
		return loadAll(target, from, null, null);
	}
	
	public synchronized LinkedList <ObjectTemplate> loadAll(Class <?> target, Integer from, Predicate <ObjectTemplate> predicate) {
		return loadAll(target, from, null, predicate, null);
	}
	
	public synchronized LinkedList <ObjectTemplate> loadAll(Class <?> target, Integer from, Predicate <ObjectTemplate> predicate, Object caller) {
		return loadAll(target, from, null, predicate, caller);
	}
	
	public synchronized LinkedList <ObjectTemplate> loadAll(Class <?> target, Integer from, Integer range) {
		return loadAll(target, from, range, null, null);
	}
	
	public synchronized LinkedList <ObjectTemplate> loadAll(Class <?> target, Integer from, Integer range, Predicate <ObjectTemplate> predicate, Object caller) {
		try {
			File folder = new File(DATA_FOLDER.getPath() + File.separator + target.getField("NAME").get(null));
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
					if((element = loadId(target, name, caller)) == null) {
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
		} catch (IllegalArgumentException | SecurityException | IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		
		return new LinkedList <ObjectTemplate> ();
	}
	
	public synchronized boolean deleteAll(Class <?> target, Predicate <ObjectTemplate> predicate) {
		return deleteAll(target, 0, null, predicate, null);
	}
	
	public synchronized boolean deleteAll(Class <?> target, Predicate <ObjectTemplate> predicate, Object caller) {
		return deleteAll(target, 0, null, predicate, caller);
	}
	
	public synchronized boolean deleteAll(Class <?> target) {
		return deleteAll(target, 0, null, null);
	}
	
	public synchronized boolean deleteAll(Class <?> target, Integer from) {
		return deleteAll(target, from, null, null);
	}
	
	public synchronized boolean deleteAll(Class <?> target, Integer from, Predicate <ObjectTemplate> predicate) {
		return deleteAll(target, from, null, predicate, null);
	}
	
	public synchronized boolean deleteAll(Class <?> target, Integer from, Predicate <ObjectTemplate> predicate, Object caller) {
		return deleteAll(target, from, null, predicate, caller);
	}
	
	public synchronized boolean deleteAll(Class <?> target, Integer from, Integer range) {
		return deleteAll(target, from, range, null, null);
	}
	
	public synchronized boolean deleteAll(Class <?> target, Integer from, Integer range, Predicate <ObjectTemplate> predicate, Object caller) {
		boolean output = true;
		LinkedList <ObjectTemplate> objectTemplates = loadAll(target, from, range, predicate, caller);
		for(ObjectTemplate objectTemplate : objectTemplates) {
			if(!deleteId(target, objectTemplate.getId())) {
				output = false;
			}
		}
		return output;
	}
	
	public synchronized ObjectTemplate load(Class <?> target, String id) {
		if(id != null) {
			return loadId(target, encrypt(id), null);
		}
		return null;
	}
	
	public synchronized ObjectTemplate load(Class <?> target, String id, Object caller) {
		if(id != null) {
			return loadId(target, encrypt(id), caller);
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
		objectTemplate.checkIfUpdated(this);
		if(objectTemplate.checkVersion(false)) {
			try {
				objectTemplate.render();
				objectTemplate.resetSave();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public synchronized boolean update(ObjectTemplate objectTemplate) {
		
		objectTemplate.checkIfUpdated(this);
		if(objectTemplate.checkVersion(true)) {
			try {
				objectTemplate.render();
				objectTemplate.resetSave();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public int getCount(Class <?> target) {
		File folder;
		try {
			folder = new File(DATA_FOLDER.getPath() + File.separator + target.getField("NAME").get(null));
			return folder.listFiles().length;
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int getNext(Class <?> target) {
		File folder;
		try {
			folder = new File(DATA_FOLDER.getPath() + File.separator + target.getField("NAME").get(null));
			folder.getParentFile().mkdirs();
			File[] files = folder.listFiles();
			if(files == null || files.length == 0) {
				return 0;
			}
			Arrays.sort(files);
			String name = files[files.length - 1].getName();
			return Integer.valueOf(name.substring(0, name.lastIndexOf(".")), COUNTER_LENGTH).intValue() + 1;
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public File getFile(Class <?> target, String id) {
		File file = null;
		try {
			file = new File(DATA_FOLDER.getPath() + File.separator + target.getField("NAME").get(null) + File.separator + id + "." + ENDING);
			file.getParentFile().mkdirs();
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
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
