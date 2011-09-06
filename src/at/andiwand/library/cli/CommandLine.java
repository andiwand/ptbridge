package at.andiwand.library.cli;

import java.io.InputStream;
import java.io.OutputStream;


public interface CommandLine {
	
	public InputStream getInputStream();
	public OutputStream getOutputStream();
	
	public void close();
	
}