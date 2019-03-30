package server;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public interface Responder {

	public Response error(int code, String message, LinkedList <String> languages) throws IOException;
	public Response error(int code, String message, LinkedList <String> languages, String error) throws IOException;
	public Response text(String text) throws IOException;
	public Response file(File file) throws IOException;
	public Response file(File file, int statusCode) throws IOException;
	public Response file(String name, int statusCode) throws IOException;
	public Response file(String name) throws IOException;
	public Response redirect(String path) throws IOException;
	public Response next() throws IOException;

}
