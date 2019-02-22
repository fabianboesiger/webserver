package server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Response {
	
	public static final String ENCODING = "UTF-8";
	
	protected InputStream inputStream;
	protected long size;
	protected String contentType;
	protected int statusCode;
	protected HashMap <String, String> responseHeaders;
	boolean next;
	
	public Response(String text, String contentType, int statusCode, HashMap <String, String> responseHeaders, boolean next) throws IOException {
		this(new ByteArrayInputStream(text.getBytes(ENCODING)), text.getBytes(ENCODING).length, contentType, statusCode, responseHeaders, next);
	}

	public Response(InputStream inputStream, long size, String contentType, int statusCode, HashMap <String, String> responseHeaders, boolean next) {
		this.inputStream = inputStream;
		this.size = size;
		this.contentType = contentType;
		this.statusCode = statusCode;
		this.responseHeaders = responseHeaders;
		this.next = next;
	}
	
}
