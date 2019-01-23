package server.loaders;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import server.handlers.Response;
import server.loaders.parser.Parser;

public class ViewsLoader extends Loader {

	public ViewsLoader(File file) throws IOException {
		super(file);
	}
	
	public ViewsLoader(String path) throws IOException {
		super(new File(path));
	}

	@Override
	public Response load() throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file);
		return new Response(parse(fileInputStream), file.length(), Files.probeContentType(Paths.get(file.getAbsolutePath())));
	}
	
	private static InputStream parse(InputStream inputStream) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, ENCODING));
	    	StringBuilder stringBuilder = new StringBuilder();
	        String line;
	        while((line = bufferedReader.readLine()) != null) {
	        	stringBuilder.append(line);
	        	stringBuilder.append("\n");
	        }
	        bufferedReader.close();
	        return new ByteArrayInputStream(Parser.parse(stringBuilder).toString().getBytes(ENCODING));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
