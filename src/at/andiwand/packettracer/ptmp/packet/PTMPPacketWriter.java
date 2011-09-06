package at.andiwand.packettracer.ptmp.packet;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

import at.andiwand.library.steam.XOROutputStream;
import at.andiwand.packettracer.ptmp.PTMPConfiguration;
import at.andiwand.packettracer.ptmp.PTMPDataOutputStream;


//TODO fix compression
public class PTMPPacketWriter {
	
	private int encoding;
	private int encryption;
	private int compression;
	
	private PTMPDataOutputStream dataOutputStream;
	
	private byte[] encryptionKey;
	
	
	
	public PTMPPacketWriter(OutputStream outputStream) {
		this(outputStream, PTMPConfiguration.DEFAULT);
	}
	public PTMPPacketWriter(OutputStream outputStream, int encoding,
			int encryption, int compression) {
		this.encoding = encoding;
		this.encryption = encryption;
		this.compression = compression;
		
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
				outputStream);
		dataOutputStream = new PTMPDataOutputStream(bufferedOutputStream,
				encoding);
	}
	public PTMPPacketWriter(OutputStream outputStream,
			PTMPConfiguration configuration) {
		this(outputStream, configuration.encoding(), configuration.encryption(),
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
	public byte[] getEncryptionKey() {
		return encryptionKey;
	}
	
	public void setEncoding(int encoding) {
		if (!PTMPConfiguration.legalEncoding(encoding))
			throw new RuntimeException("Unknown encoding: " + encoding);
		
		this.encoding = encoding;
		
		dataOutputStream.setEncoding(encoding);
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
	public void setEncryptionKey(byte[] encryptionKey) {
		this.encryptionKey = encryptionKey;
	}
	
	
	public void writePacket(PTMPPacket packet) throws IOException {
		byte[] data = packet.getBytes(encoding);
		
		ByteArrayOutputStream packetOutputStream = new ByteArrayOutputStream();
		OutputStream outputStream = packetOutputStream;
		
		switch (encryption) {
		case PTMPConfiguration.ENCRYPTION_NONE:
			break;
		case PTMPConfiguration.ENCRYPTION_XOR:
			outputStream = new XOROutputStream(outputStream, encryptionKey);
			break;
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
		
		switch (compression) {
		case PTMPConfiguration.COMPRESSION_NO:
			break;
		case PTMPConfiguration.COMPRESSION_ZLIB_DEFAULT:
			outputStream = new ZipOutputStream(outputStream);
			break;
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
		
		outputStream.write(data);
		byte[] packetBytes = packetOutputStream.toByteArray();
		
		synchronized (this) {
			dataOutputStream.writeInt(packetBytes.length);
			dataOutputStream.write(packetBytes);
			dataOutputStream.flush();
		}
	}
	
}