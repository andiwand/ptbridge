package at.andiwand.packettracer.ptmp.packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

import at.andiwand.library.steam.XORInputStream;
import at.andiwand.packettracer.ptmp.PTMPConfiguration;
import at.andiwand.packettracer.ptmp.PTMPDataInputStream;


//TODO fix decompression
public class PTMPPacketReader {
	
	private int encoding;
	private int encryption;
	private int compression;
	
	private PTMPDataInputStream dataInputStream;
	
	private byte[] decryptionKey;
	
	
	
	public PTMPPacketReader(InputStream inputStream) {
		this(inputStream, PTMPConfiguration.DEFAULT);
	}
	public PTMPPacketReader(InputStream inputStream, int encoding,
			int encryption, int compression) {
		this.encoding = encoding;
		this.encryption = encryption;
		this.compression = compression;
		
		dataInputStream = new PTMPDataInputStream(inputStream, encoding);
	}
	public PTMPPacketReader(InputStream inputStream,
			PTMPConfiguration configuration) {
		this(inputStream, configuration.encoding(), configuration.encryption(),
				configuration.compression());
	}
	
	
	public int encoding() {
		return encoding;
	}
	public int encryption() {
		return encryption;
	}
	public int compression() {
		return compression;
	}
	public byte[] getDecryptionKey() {
		return decryptionKey;
	}
	
	public void setEncoding(int encoding) {
		if (!PTMPConfiguration.legalEncoding(encoding))
			throw new RuntimeException("Unknown encoding: " + encoding);
		
		this.encoding = encoding;
		
		dataInputStream.setEncoding(encoding);
	}
	public void setEncryption(int encryption) {
		if (!PTMPConfiguration.legalEncryption(encryption))
			throw new RuntimeException("Unknown encryption: " + encryption);
		
		this.encryption = encryption;
	}
	public void setCompression(int compression) {
		if (!PTMPConfiguration.legalCompression(compression))
			throw new RuntimeException("Unknown compression: " + compression);
		
		this.compression = compression;
	}
	public void setConfiguration(int encoding, int encryption, int compression) {
		setEncoding(encoding);
		setEncryption(encryption);
		setCompression(compression);
	}
	public void setConfiguration(PTMPConfiguration configuration) {
		setConfiguration(configuration.encoding(), configuration.encryption(),
				configuration.compression());
	}
	public void setDecryptionKey(byte[] decryptionKey) {
		this.decryptionKey = decryptionKey;
	}
	
	
	public PTMPEncodedPacket readPacket() throws IOException {
		int length;
		byte[] data;
		
		synchronized (this) {
			length = dataInputStream.readInt();
			
			data = new byte[length];
			dataInputStream.read(data);
		}
		
		
		InputStream inputStream = new ByteArrayInputStream(data);
		
		switch (encryption) {
		case PTMPConfiguration.ENCRYPTION_NONE:
			break;
		case PTMPConfiguration.ENCRYPTION_XOR:
			inputStream = new XORInputStream(inputStream, decryptionKey);
			break;
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
		
		switch (compression) {
		case PTMPConfiguration.COMPRESSION_NO:
			break;
		case PTMPConfiguration.COMPRESSION_ZLIB_DEFAULT:
			inputStream = new ZipInputStream(inputStream);
			break;
		
		default:
			throw new IllegalStateException("Unreachable section");
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