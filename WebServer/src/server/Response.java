package server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import application.Application;

public class Response {
	
	public static final String ENCODING = "UTF-8";
	
	protected InputStream inputStream;
	protected long size;
	protected String contentType;
	protected int statusCode;
	protected HashMap <String, String> responseHeaders;
	boolean next;
	
	public Response(Application application, String text, String contentType, int statusCode, HashMap <String, String> responseHeaders, boolean next) throws IOException {
		this(application, new ByteArrayInputStream(text.getBytes(ENCODING)), text.getBytes(ENCODING).length, contentType, statusCode, responseHeaders, next);
	}

	public Response(Application application, InputStream inputStream, long size, String contentType, int statusCode, HashMap <String, String> responseHeaders, boolean next) {
		this.inputStream = inputStream;
		this.size = size;
		this.contentType = contentType;
		this.statusCode = statusCode;
		this.responseHeaders = responseHeaders;
		this.next = next;
	}
	
}
