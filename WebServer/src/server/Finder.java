package server;

import java.io.File;
import java.io.IOException;

public abstract class Finder {
	
	public static void find(File folder, FinderAction finderAction) throws IOException {
		if(folder.exists()) {
			for(File file: folder.listFiles()) {
				if(file.isDirectory()) {
					find(file, finderAction);
				} else {
					finderAction.act(file);
				}
			}
		}
	}
	
}
