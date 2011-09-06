package at.andiwand.library.steam;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;


public class CountingInputStream extends FilterInputStream {
	
	private int count;
	
	
	public CountingInputStream(InputStream in) {
		super(in);
	}
	
	
	public int count() {
		return count;
	}
	
	
	@Override
	public int read() throws IOException {
		int read = in.read();
		
		if (read == -1) return -1;
		
		count++;
		return read;
	}
	@Override
	public int read(byte[] b) throws IOException {
		int read = in.read(b);
		
		count += read;
		return read;
	}
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int read = in.read(b, off, len);
		
		count += read;
		return read;
	}
	
}