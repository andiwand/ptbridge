package at.andiwand.library.steam;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class XOROutputStream extends FilterOutputStream {
	
	private byte[] key;
	private int index;
	
	
	
	public XOROutputStream(OutputStream out, byte[] key) {
		super(out);
		
		this.key = key;
	}
	
	
	
	public byte[] getKey() {
		return key;
	}
	
	public void setKey(byte[] key) {
		this.key = key;
	}
	
	
	public void write(int b) throws IOException {
		out.write(b ^ key[index]);
		
		index = (index + 1) % key.length;
	}
	public void write(byte[] b) throws IOException {
		write(b, 0, b.length);
	}
	public void write(byte[] b, int off, int len) throws IOException {
		for (int i = 0; i < len; i++) {
			write(b[off + i]);
		}
	}
	
}