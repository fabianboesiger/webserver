package server;

public interface Session <T> {
	
	public String getSessionId();
	public boolean expired();
	public boolean active();
	public void update();
	public void save(T object);
	public void delete();
	public T load();
	public void addFlash(Object value);
	public Object getFlash(String key);
	
}
