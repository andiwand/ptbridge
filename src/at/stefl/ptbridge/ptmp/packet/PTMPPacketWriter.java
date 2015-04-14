package at.stefl.ptbridge.ptmp.packet;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

import at.stefl.commons.io.XOROutputStream;
import at.stefl.ptbridge.ptmp.PTMPCompression;
import at.stefl.ptbridge.ptmp.PTMPConfiguration;
import at.stefl.ptbridge.ptmp.PTMPDataOutputStream;
import at.stefl.ptbridge.ptmp.PTMPEncoding;
import at.stefl.ptbridge.ptmp.PTMPEncryption;


// TODO fix compression
public class PTMPPacketWriter {
	
	private PTMPEncoding encoding;
	private PTMPEncryption encryption;
	private PTMPCompression compression;
	
	private PTMPDataOutputStream out;
	
	private byte[] encryptionKey;
	
	public PTMPPacketWriter(OutputStream outputStream) {
		this(outputStream, PTMPConfiguration.DEFAULT);
	}
	
	public PTMPPacketWriter(OutputStream out, PTMPEncoding encoding,
			PTMPEncryption encryption, PTMPCompression compression) {
		this.encoding = encoding;
		this.encryption = encryption;
		this.compression = compression;
		
		this.out = new PTMPDataOutputStream(new BufferedOutputStream(out),
				encoding);
	}
	
	public PTMPPacketWriter(OutputStream out, PTMPConfiguration configuration) {
		this(out, configuration.getEncoding(), configuration.getEncryption(),
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
	
	public byte[] getEncryptionKey() {
		return encryptionKey;
	}
	
	public void setEncoding(PTMPEncoding encoding) {
		this.encoding = encoding;
		
		out.setEncoding(encoding);
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
	
	public void setEncryptionKey(byte[] encryptionKey) {
		this.encryptionKey = encryptionKey;
	}
	
	public void writePacket(PTMPPacket packet) throws IOException {
		byte[] data = packet.getBytes(encoding);
		
		ByteArrayOutputStream packetOutputStream = new ByteArrayOutputStream();
		OutputStream outputStream = packetOutputStream;
		
		switch (encryption) {
		case NONE:
			break;
		case XOR:
			outputStream = new XOROutputStream(outputStream, encryptionKey);
			break;
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
		
		switch (compression) {
		case NO:
			break;
		case ZLIB_DEFAULT:
			outputStream = new ZipOutputStream(outputStream);
			break;
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
		
		outputStream.write(data);
		byte[] packetBytes = packetOutputStream.toByteArray();
		
		synchronized (this) {
			out.writeInt(packetBytes.length);
			out.write(packetBytes);
			out.flush();
		}
	}
	
}