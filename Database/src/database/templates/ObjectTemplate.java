package database.templates;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class ObjectTemplate extends Template {
	
	private transient PrimitiveTemplate idTemplate;
	private transient LinkedList <String> order;
	
	public ObjectTemplate(String name) {
		super(name);
	}
	
	public void addToOrder(String name) {
		order.add(name);
	}
	
	public String getName() {
		return name;
	}
	
	public void setFromStringMap(Map <String, String> input) {
		HashMap <String, Object> output = new HashMap <String, Object> ();
		for(Map.Entry <String, String> entry : input.entrySet()) {
			output.put(entry.getKey(), entry.getValue());
		}
		set(output);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void set(Object object) {
		Map <String, Object> input = (Map <String, Object>) object;
		Field[] fields = getClass().getDeclaredFields();
		for(Field field : fields) {
			String name = field.getName();
			if(input.containsKey(name)) {
				field.setAccessible(true);
				try {
					if(field.get(this) instanceof Template) {
						((Template) field.get(this)).set(input.get(name));
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public Object get() {
		HashMap <String, Object> output = new HashMap <String, Object> ();
		Field[] fields = getClass().getDeclaredFields();
		for(Field field : fields) {
			try {
				if(!Modifier.isTransient(field.getModifiers())) {
					output.put(field.getName(), ((Template) field.get(this)).get());
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
	
	@SuppressWarnings("unchecked")
	@Override 
	public String toCSV() {
		StringBuilder stringBuilder = new StringBuilder();
		Set <Map.Entry <String, Object>> map = ((HashMap <String, Object>) get()).entrySet();
		for(int i = 0; i < map.size(); i++) {
			return stringBuilder.toString();
		}
		return name;
	}

	@Override
	public void fromCSV(String string) {
		// TODO Auto-generated method stub
		
	}
	
}
