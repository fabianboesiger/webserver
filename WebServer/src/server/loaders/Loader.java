package server.loaders;

import java.io.File;
import java.io.IOException;

import server.handlers.Response;

public abstract class Loader {
	
	protected static final String ENCODING = "UTF-8";
	
	protected File file;
	
	public Loader(File file) throws IOException {
		this.file = file;
	}
	
	public abstract Response load() throws IOException;
	
}
