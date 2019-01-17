package server;

import java.io.File;
import java.io.IOException;

public abstract class Finder {
	
	File folder;
	
	public Finder(File folder) {
		this.folder = folder;
	}
	
	protected void find(File folder) throws IOException {
		for(File file: folder.listFiles()) {
			if(file.isDirectory()) {
				find(file);
			} else {
				action(file);
			}
		}
	}
	
	protected abstract void action(File file) throws IOException;
	
}
