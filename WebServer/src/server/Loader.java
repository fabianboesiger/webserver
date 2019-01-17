package server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import server.parser.Parser;

public class Loader {
	
	private static final String ENCODING = "UTF-8";
	private static final String HTML_CONTENT_TYPE = "text/html";
	
	private File file;
	private String contentType;
	private InputStream inputStream;
	private long fileLength;
	
	public Loader(File file) throws IOException {
		this.file = file;
		contentType = Files.probeContentType(Paths.get(file.getAbsolutePath()));
		inputStream = null;
		fileLength = 0;
	}
	
	public Loader(String path) throws IOException {
		this(new File(path));
	}

	public void load() {
		try {
	    	FileInputStream fileInputStream = new FileInputStream(file);
	        
	        if(contentType != null && contentType.equals(HTML_CONTENT_TYPE)) {
	            byte[] data = Parser.parse(buildString(fileInputStream)).getBytes(ENCODING);
	            fileLength = data.length;
	            inputStream = new ByteArrayInputStream(data);
	        } else {
	        	fileLength = file.length();
	        	inputStream = fileInputStream;
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public StringBuilder buildString(InputStream inputStream) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, ENCODING));
	    	StringBuilder stringBuilder = new StringBuilder();
	        String line;
	        while((line = bufferedReader.readLine()) != null) {
	        	stringBuilder.append(line);
	        	stringBuilder.append("\n");
	        }
	        bufferedReader.close();
	        return stringBuilder;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}
	
	public long getFileLength() {
		return fileLength;
	}
	
}
