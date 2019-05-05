package manager;

import database.templates.IdentifiableStringTemplate;
import database.templates.ListTemplate;
import database.templates.LongTemplate;
import database.templates.ObjectTemplate;
import database.templates.ObjectTemplateReference;
import database.validator.Validator;
import server.Session;

public class DatabaseSession <T extends ObjectTemplate> extends ObjectTemplate implements Session <T> {
	
	public static final String NAME = "sessions";
	
	private static final int ACTIVE = 5 * 60;
	
	private IdentifiableStringTemplate id;
	private LongTemplate lastConnect;
	private ObjectTemplateReference <T> object;
	private ListTemplate <Validator> flashes;
	
	private DatabaseSessionManager <T> databaseSessionManager;
	
	public DatabaseSession(DatabaseSessionManager <T> databaseSessionManager) {
		this.databaseSessionManager = databaseSessionManager;
		id = new IdentifiableStringTemplate("id");
		lastConnect = new LongTemplate("connect");
		flashes = new ListTemplate <Validator> ("flashes", Validator::new);
		if(databaseSessionManager != null) {
			object = new ObjectTemplateReference <T> ("object", databaseSessionManager.supplier);
		}
		setIdentifier(this.id);
		update();
	}
	
	public DatabaseSession() {
		this(null);
	}
	
	protected void setSessionId(String id) {
		this.id.set(id);
	}

	@Override
	public String getSessionId() {
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
	public void save(T object) {
		this.object.set(object);
		databaseSessionManager.database.update(this);
	}

	@Override
	public void delete() {
		object = null;
		databaseSessionManager.database.update(this);
	}

	@Override
	public T load() {
		if(object != null) {
			return (T) object.get();
		} else {
			return null;
		}
	}

	@Override
	public void addFlash(Object value) {System.out.println("!1");
		flashes.add((Validator) value);System.out.println("!2 "+databaseSessionManager+" "+databaseSessionManager.database);
		databaseSessionManager.database.update(this);System.out.println("!3");
	}

	@Override
	public Object getFlash(String name) {
		for(Validator flash : flashes) {
			if(flash.getName().equals(name)) {
				flashes.remove(flash);
				flash.delete(databaseSessionManager.database);
				databaseSessionManager.database.update(this);
				return flash;
			}
		}
		return null;
	}

}
