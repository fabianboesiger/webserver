package server.handlers;

import java.io.InputStream;

public class Response {
	
	InputStream inputStream;
	long size;
	String contentType;
	
	public Response(InputStream inputStream, long size, String contentType) {
		this.inputStream = inputStream;
		this.size = size;
		this.contentType = contentType;
	}
	
}
