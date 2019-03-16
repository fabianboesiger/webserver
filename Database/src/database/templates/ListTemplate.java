package database.templates;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import database.Database;
import database.Messages;

public class ListTemplate <T extends Template> extends Template implements List <T> {
	
	private static final char LIST_START = '[';
	private static final char LIST_END = ']';
	
	ArrayList <T> list;
	private transient boolean notNull;
	private transient Integer maximumSize;
	private transient Integer minimumSize;
	
	public ListTemplate(String name, Integer minimumSize, Integer maximumSize, boolean notNull) {
		super(name);
		this.notNull = notNull;
	}
	
	public ListTemplate(String name, Integer minimumSize, Integer maximumSize) {
		this(name, minimumSize, maximumSize, true);
	}
	
	public ListTemplate(String name) {
		this(name, null, null);
	}
	
	public ListTemplate() {
		this(null);
	}

	@Override
	public String render(Database database) throws Exception {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(LIST_START);
		for(int i = 0; i < list.size(); i++) {
			if(i != 0) {
				stringBuilder.append(SEPARATION_CHARACTER);
			}
			stringBuilder.append(list.get(i).render(database));
		}
		stringBuilder.append(LIST_END);
		return null;
	}

	@Override
	public void parse(Database database, StringBuilder string, Map <String, ObjectTemplate> initialized) throws Exception {
		String trimmed = string.toString().trim();
		StringBuilder content = new StringBuilder(trimmed.substring(1, trimmed.length() - 1));
		while(content.length() > 0) {
			
			T element = createInstance();
			element.parse(database, string, initialized);
			list.add(element);
			
			while(string.length() > 0) {
				if(string.charAt(0) == SEPARATION_CHARACTER) {
					string.deleteCharAt(0);
					break;
				}
				string.deleteCharAt(0);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private T createInstance() {
		try {
			return (T) ((Class <?>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.getCause();
		}
		return null;
	}

	@Override
	public boolean validate(Messages messages) {
		boolean valid = true;
		if(updated) {
			if(list == null) {
				if(notNull) {
					valid = false;
					messages.add(name, "not-initialized");
				}
			} else {
				if(minimumSize != null) {
					if(list.size() < minimumSize) {
						valid = false;
						messages.add(name, "minimum-elements-exceeded");
					}
				}
				if(maximumSize != null) {
					if(list.size() > maximumSize) {
						valid = false;
						messages.add(name, "maximum-elements-exceeded");
					}
				}
			}
		}
		return valid;
	}
	
	@Override
	public void update() {
		updated = true;
		for(int i = 0; i < list.size(); i++) {
			list.get(i).update();
		}
	}

	@Override
	public boolean add(T arg0) {
		update();
		return list.add(arg0);
	}

	@Override
	public void add(int arg0, T arg1) {
		update();
		list.add(arg0, arg1);
	}

	@Override
	public boolean addAll(Collection<? extends T> arg0) {
		update();
		return addAll(arg0);
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends T> arg1) {
		update();
		return addAll(arg0, arg1);
	}

	@Override
	public void clear() {
		update();
		list.clear();
	}

	@Override
	public boolean contains(Object arg0) {
		return list.contains(arg0);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return list.containsAll(arg0);
	}

	@Override
	public T get(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int indexOf(Object arg0) {
		return list.indexOf(arg0);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

	@Override
	public int lastIndexOf(Object arg0) {
		return list.lastIndexOf(arg0);
	}

	@Override
	public ListIterator<T> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int arg0) {
		return list.listIterator(arg0);
	}

	@Override
	public boolean remove(Object arg0) {
		update();
		return list.remove(arg0);
	}

	@Override
	public T remove(int arg0) {
		update();
		return list.remove(arg0);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		update();
		return list.removeAll(arg0);
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		update();
		return list.retainAll(arg0);
	}

	@Override
	public T set(int arg0, T arg1) {
		update();
		return list.set(arg0, arg1);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public List<T> subList(int arg0, int arg1) {
		return list.subList(arg0, arg1);
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] arg0) {
		return list.toArray(arg0);
	}


}
