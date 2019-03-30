package server;

public interface Session <T> {
	
	public String getId();
	public boolean expired();
	public boolean active();
	public void update();
	public void addFlash(String key, Object value);
	public Object getFlash(String key);
	public void save(T object);
	public void delete();
	public T load();
	
}
