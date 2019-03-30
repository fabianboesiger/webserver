package manager;

import java.util.HashMap;

import database.templates.IdentifiableStringTemplate;
import database.templates.LongTemplate;
import database.templates.ObjectTemplate;
import database.templates.ObjectTemplateReference;
import server.Session;

public class DatabaseSession <T extends ObjectTemplate> extends ObjectTemplate implements Session <T> {
	
	public static final String NAME = "sessions";
	
	private static final int ACTIVE = 5 * 60;
	
	private IdentifiableStringTemplate id;
	private LongTemplate lastConnect;
	private ObjectTemplateReference <T> object;
	
	private HashMap <String, Object> flashes;
	private DatabaseSessionManager <T> databaseSessionManager;
	
	public DatabaseSession(DatabaseSessionManager <T> databaseSessionManager) {
		this.databaseSessionManager = databaseSessionManager;
		id = new IdentifiableStringTemplate("id");
		lastConnect = new LongTemplate("connect");
		if(databaseSessionManager != null) {
			object = new ObjectTemplateReference <T> ("object", databaseSessionManager.supplier);
		}
		flashes = new HashMap <String, Object> ();
		setIdentifier(this.id);
		update();
	}
	
	public DatabaseSession() {
		this(null);
	}
	
	protected void setId(String id) {
		this.id.set(id);
	}

	@Override
	public String getId() {
		return (String) id.get();
	}
	
	@Override
	public boolean expired() {
		if(databaseSessionManager != null) {
			if(System.currentTimeMillis() >= ((Long) lastConnect.get()) + databaseSessionManager.getMaxSessionAge() * 1000) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean active() {
		if(System.currentTimeMillis() < ((Long) lastConnect.get()) + ACTIVE * 1000) {
			return true;
		}
		return false;
	}
	
	@Override
	public void update() {
		lastConnect.set(System.currentTimeMillis());
	}
	
	@Override
	public void addFlash(String key, Object value) {
		flashes.put(key, value);
	}
	
	@Override
	public Object getFlash(String key) {
		return flashes.remove(key);
	}

	@Override
	public void save(T object) {
		this.object.set(object);
		databaseSessionManager.database.update(this);
	}

	@Override
	public void delete() {
		object = null;
		databaseSessionManager.database.update(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T load() {
		if(object != null) {
			return (T) object.get();
		} else {
			return null;
		}
	}
}
