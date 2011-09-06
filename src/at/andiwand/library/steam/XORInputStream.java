package at.andiwand.library.steam;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;


public class XORInputStream extends FilterInputStream {
	
	private byte[] key;
	private int index;
	
	
	
	public XORInputStream(InputStream in, byte[] key) {
		super(in);
		
		this.key = key;
	}
	
	
	
	public byte[] getKey() {
		return key;
	}
	
	public void setKey(byte[] key) {
		this.key = key;
	}
	
	
	public int read() throws IOException {
		int read = in.read();
		if (read == -1) return -1;
		
		int result = read ^ key[index];
		index = (index + 1) % key.length;
		
		return result;
	}
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}
	public int read(byte[] b, int off, int len) throws IOException {
		for (int i = 0; i < len; i++) {
			int read = read();
			if (read == -1) return i;
			
			b[off + i] = (byte) read;
		}
		
		return len;
	}
	
}