package database.templates;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import database.Database;

public class ObjectTemplate extends Template {
	
	private transient Identifiable identifier;
	private transient LinkedList <String> order;
	
	public ObjectTemplate(String name) {
		super(name);
		identifier = null;
	}
	
	public void addToOrder(String name) {
		order.add(name);
	}
	
	public void setIdentifier(Identifiable identifier) {
		this.identifier = identifier;
	}
	
	public Identifiable getIdentifier() {
		return identifier;
	}
	
	public String getName() {
		return name;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void set(Object object) {
		Map <String, Object> input = (Map <String, Object>) object;
		Field[] fields = getClass().getDeclaredFields();
		for(Field field : fields) {
			try {
				field.setAccessible(true);
				if(field.get(this) instanceof Template) {
					String name = ((Template) field.get(this)).name;
					if(input.containsKey(name)) {
						((Template) field.get(this)).set(input.get(name));
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public Object get() {
		HashMap <String, Object> output = new HashMap <String, Object> ();
		Field[] fields = getClass().getDeclaredFields();
		for(Field field : fields) {
			field.setAccessible(true);
			try {
				if(field.get(this) instanceof Template) {
					if(!Modifier.isTransient(field.getModifiers())) {
						output.put(((Template) field.get(this)).name, ((Template) field.get(this)).get());
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return output;
	}
	
	@Override
	public boolean validate(Errors errors) {
		boolean valid = true;
		Field[] fields = getClass().getDeclaredFields();
		for(Field field : fields) {
			field.setAccessible(true);
			try {
				if(field.get(this) instanceof Template) {
					if(!((Template) field.get(this)).validate(errors)) {
						valid = false;
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				return false;
			}
		}
		return valid;
	}

	public LinkedList <String> render(Database database) {
		LinkedList <String> lines = new LinkedList <String> ();
		Field[] fields = getClass().getDeclaredFields();
		for(int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			try {
				Object object = field.get(this);
				if(!Modifier.isTransient(field.getModifiers())) {
					if(object instanceof PrimitiveTemplate) {
						lines.add(((PrimitiveTemplate) object).name + "=" + ((PrimitiveTemplate) object).render());
					} else
					if(object instanceof ObjectTemplate) {
						lines.add(((PrimitiveTemplate) object).name + "=" + database.save((ObjectTemplate) object));
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return lines;
	}
	
	public void parse(Map <String, String> input) {
		parse(null, input);
	}

	public void parse(Database database, Map <String, String> input) {
		Field[] fields = getClass().getDeclaredFields();
		for(Field field : fields) {
			try {
				field.setAccessible(true);
				Object object = field.get(this);
				if(object instanceof Template) {
					String name = ((Template) object).name;
					if(input.containsKey(name)) {
						if(object instanceof PrimitiveTemplate) {
							((PrimitiveTemplate) field.get(this)).parse(input.get(name));
						} else
						if(object instanceof ObjectTemplate) {
							if(database != null) {
								database.loadId((ObjectTemplate) field.get(this), input.get(name));
							}
						}
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
}
