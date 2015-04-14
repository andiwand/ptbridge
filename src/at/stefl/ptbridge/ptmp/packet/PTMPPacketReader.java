package at.stefl.ptbridge.ptmp.packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

import at.stefl.commons.io.XORInputStream;
import at.stefl.ptbridge.ptmp.PTMPCompression;
import at.stefl.ptbridge.ptmp.PTMPConfiguration;
import at.stefl.ptbridge.ptmp.PTMPDataInputStream;
import at.stefl.ptbridge.ptmp.PTMPEncoding;
import at.stefl.ptbridge.ptmp.PTMPEncryption;


// TODO fix decompression
public class PTMPPacketReader {
	
	private PTMPEncoding encoding;
	private PTMPEncryption encryption;
	private PTMPCompression compression;
	
	private PTMPDataInputStream in;
	
	private byte[] decryptionKey;
	
	public PTMPPacketReader(InputStream inputStream) {
		this(inputStream, PTMPConfiguration.DEFAULT);
	}
	
	public PTMPPacketReader(InputStream in, PTMPEncoding encoding,
			PTMPEncryption encryption, PTMPCompression compression) {
		this.encoding = encoding;
		this.encryption = encryption;
		this.compression = compression;
		
		this.in = new PTMPDataInputStream(in, encoding);
	}
	
	public PTMPPacketReader(InputStream in, PTMPConfiguration configuration) {
		this(in, configuration.getEncoding(), configuration.getEncryption(),
				configuration.getCompression());
	}
	
	public PTMPEncoding getEncoding() {
		return encoding;
	}
	
	public PTMPEncryption getEncryption() {
		return encryption;
	}
	
	public PTMPCompression getCompression() {
		return compression;
	}
	
	public byte[] getDecryptionKey() {
		return decryptionKey;
	}
	
	public void setEncoding(PTMPEncoding encoding) {
		this.encoding = encoding;
		
		in.setEncoding(encoding);
	}
	
	public void setEncryption(PTMPEncryption encryption) {
		this.encryption = encryption;
	}
	
	public void setCompression(PTMPCompression compression) {
		this.compression = compression;
	}
	
	public void setConfiguration(PTMPEncoding encoding,
			PTMPEncryption encryption, PTMPCompression compression) {
		setEncoding(encoding);
		setEncryption(encryption);
		setCompression(compression);
	}
	
	public void setConfiguration(PTMPConfiguration configuration) {
		setConfiguration(configuration.getEncoding(), configuration
				.getEncryption(), configuration.getCompression());
	}
	
	public void setDecryptionKey(byte[] decryptionKey) {
		this.decryptionKey = decryptionKey;
	}
	
	public PTMPEncodedPacket readPacket() throws IOException {
		int length;
		byte[] data;
		
		synchronized (this) {
			length = in.readInt();
			
			data = new byte[length];
			in.read(data);
		}
		
		InputStream inputStream = new ByteArrayInputStream(data);
		
		switch (encryption) {
		case NONE:
			break;
		case XOR:
			inputStream = new XORInputStream(inputStream, decryptionKey);
			break;
		default:
			throw new IllegalStateException("Unsupported encryption!");
		}
		
		switch (compression) {
		case NO:
			break;
		case ZLIB_DEFAULT:
			inputStream = new ZipInputStream(inputStream);
			break;
		default:
			throw new IllegalStateException("Unsupported encryption!");
		}
		
		PTMPDataInputStream dataInputStream = new PTMPDataInputStream(
				inputStream, encoding);
		
		int type = dataInputStream.readInt();
		
		ByteArrayOutputStream valueOutputStream = new ByteArrayOutputStream();
		int read;
		while ((read = inputStream.read()) != -1) {
			valueOutputStream.write(read);
		}
		
		return new PTMPEncodedPacket(type, valueOutputStream.toByteArray(),
				encoding);
	}
	
}