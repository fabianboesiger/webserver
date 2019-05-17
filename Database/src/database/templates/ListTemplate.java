package database.templates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.Supplier;

import database.Database;
import database.validator.Validator;

public class ListTemplate <T extends Template> extends ComplexTemplate implements List <T> {
	
	private static final char LIST_START = '[';
	private static final char LIST_END = ']';
	
	private ArrayList <T> list;
	private Supplier <T> supplier;
	private boolean notNull;
	private Integer maximumSize;
	private Integer minimumSize;
	
	public ListTemplate(String name, Integer minimumSize, Integer maximumSize, boolean notNull, Supplier <T> supplier) {
		super(name);
		this.notNull = notNull;
		this.supplier = supplier;
		list = new ArrayList <T> ();
	}
	
	public ListTemplate(String name, Integer minimumSize, Integer maximumSize, Supplier <T> supplier) {
		this(name, minimumSize, maximumSize, true, supplier);
	}
	
	public ListTemplate(String name, Supplier <T> supplier) {
		this(name, null, null, supplier);
	}
	
	public ListTemplate(Supplier <T> supplier) {
		this(null, supplier);
	}

	@Override
	public String render() throws Exception {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(LIST_START);
		for(int i = 0; i < list.size(); i++) {
			if(i != 0) {
				stringBuilder.append(SEPARATION_CHARACTER);
			}
			stringBuilder.append(list.get(i).render());
		}
		stringBuilder.append(LIST_END);
		return stringBuilder.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void parse(Database database, StringBuilder string, Map <String, ObjectTemplate> initialized) throws Exception {
		parsed();
		this.database = database;
		list.clear();
		String trimmed = string.toString().trim();
		string.setLength(0);
		StringBuilder content = new StringBuilder(trimmed.substring(1, trimmed.length() - 1));
		while(content.length() > 0) {
			T element = supplier.get();
			if(element instanceof ObjectTemplate) {
				element = (T) checkIfInitialized((ObjectTemplate) element, database, crop(content), initialized, null);
			} else {
				element.parse(database, content, initialized);
			}
			list.add(element);
		}
	}

	@Override
	public boolean validate(Validator validator) {
		
		boolean valid = true;
		if(updated) {
			if(list == null) {
				if(notNull) {
					valid = false;
					if(validator != null) {
						validator.addMessage(templateName, "not-initialized");
					}
				}
			} else {
				if(minimumSize != null) {
					if(list.size() < minimumSize) {
						valid = false;
						if(validator != null) {
							validator.addMessage(templateName, "minimum-elements-exceeded");
						}
					}
				}
				if(maximumSize != null) {
					if(list.size() > maximumSize) {
						valid = false;
						if(validator != null) {
							validator.addMessage(templateName, "maximum-elements-exceeded");
						}
					}
				}
				for(T element : list) {
					if(!((Template) element).validate(validator)) {
						valid = false;
					}
				}
			}
		}
		return valid;
	}
	
	@Override
	public boolean validate() {
		return validate(null);
	}
	
	@Override
	public boolean add(T arg0) {
		updated();
		return list.add(arg0);
	}

	@Override
	public void add(int arg0, T arg1) {
		updated();
		list.add(arg0, arg1);
	}

	@Override
	public boolean addAll(Collection<? extends T> arg0) {
		updated();
		return addAll(arg0);
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends T> arg1) {
		updated();
		return addAll(arg0, arg1);
	}

	@Override
	public void clear() {
		updated();
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
		updated();
		return list.remove(arg0);
	}

	@Override
	public T remove(int arg0) {
		updated();
		return list.remove(arg0);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		updated();
		return list.removeAll(arg0);
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		updated();
		return list.retainAll(arg0);
	}

	@Override
	public T set(int arg0, T arg1) {
		updated();
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

	@Override
	public void checkIfUpdated(Database database) {
		this.database = database;
		for(T element : list) {
			if(element instanceof ComplexTemplate) {
				((ComplexTemplate) element).checkIfUpdated(database);
			}
			if(((Template) element).updated) {
				updated();
			}
		}
	}

	@Override
	public boolean checkVersion(boolean overwrite) {
		for(T element : list) {
			if(element instanceof ComplexTemplate) {
				if(!((ComplexTemplate) element).checkVersion(overwrite)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	protected void resetSave() {
		for(T element : list) {
			if(element instanceof ComplexTemplate) {
				((ComplexTemplate) element).resetSave();
			}
		}
	}

	@Override
	protected void resetLoad() {
		for(T element : list) {
			if(element instanceof ComplexTemplate) {
				((ComplexTemplate) element).resetLoad();
			}
		}
	}

	@Override
	public void reload() {
		T instance = supplier.get();
		if(instance instanceof ObjectTemplate) {
			for(int i = 0; i < list.size(); i++) {
				((ObjectTemplate) list.get(i)).reload();
			}
		}
		
	}

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("[");
		boolean first = true;
		for(T element : list) {
			if(!first) {
				output.append(", ");
			} else {
				first = false;
			}
			output.append(element.toString());
		}
		output.append("]");
		return output.toString();
	}
}
