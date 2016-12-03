package dev.edu.javaee.spring.resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class LocalFileResource implements Resource{
	private final String fileName;

    public LocalFileResource(String name) {
        this.fileName = name;
    }

    @Override
    public InputStream getInputStream() throws IOException{
    	return new FileInputStream(fileName);
    }
}
