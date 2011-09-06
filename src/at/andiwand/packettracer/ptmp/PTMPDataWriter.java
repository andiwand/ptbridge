package at.andiwand.packettracer.ptmp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.util.UUID;

import at.andiwand.library.network.mac.MACAddress;


public class PTMPDataWriter extends OutputStream {
	
	private int encoding;
	
	private ByteArrayOutputStream outputStream;
	private PTMPDataOutputStream dataOutputStream;
	
	
	
	public PTMPDataWriter(int encoding) {
		this.encoding = encoding;
		
		reset();
	}
	
	
	
	public byte[] getData() {
		return outputStream.toByteArray();
	}
	public int encoding() {
		return encoding;
	}
	
	
	public void write(int data) {
		try {
			dataOutputStream.write(data);
		} catch (IOException e) {}
	}
	public void write(byte[] data) {
		try {
			dataOutputStream.write(data);
		} catch (IOException e) {}
	}
	public void write(byte[] data, int offset, int length) {
		try {
			dataOutputStream.write(data, offset, length);
		} catch (IOException e) {}
	}
	
	public void writeByte(int b) {
		try {
			dataOutputStream.writeByte(b);
		} catch (IOException e) {}
	}
	public void writeBoolean(boolean b) {
		try {
			dataOutputStream.writeBoolean(b);
		} catch (IOException e) {}
	}
	public void writeShort(int s) {
		try {
			dataOutputStream.writeShort(s);
		} catch (IOException e) {}
	}
	public void writeInt(int i) {
		try {
			dataOutputStream.writeInt(i);
		} catch (IOException e) {}
	}
	public void writeLong(long l) {
		try {
			dataOutputStream.writeLong(l);
		} catch (IOException e) {}
	}
	public void writeFloat(float f) {
		try {
			dataOutputStream.writeFloat(f);
		} catch (IOException e) {}
	}
	public void writeDouble(double d) {
		try {
			dataOutputStream.writeDouble(d);
		} catch (IOException e) {}
	}
	
	public void writeString(String string) {
		try {
			dataOutputStream.writeString(string);
		} catch (IOException e) {}
	}
	
	public void writeIP4Addres(Inet4Address ip4) {
		try {
			dataOutputStream.writeIP4Addres(ip4);
		} catch (IOException e) {}
	}
	public void writeIP6Addres(Inet6Address ip6) {
		try {
			dataOutputStream.writeIP6Addres(ip6);
		} catch (IOException e) {}
	}
	
	public void writeMACAddress(MACAddress macAddress) {
		try {
			dataOutputStream.writeMACAddress(macAddress);
		} catch (IOException e) {}
	}
	
	public void writeUuid(UUID uuid) {
		try {
			dataOutputStream.writeUuid(uuid);
		} catch (IOException e) {}
	}
	
	public void writeConfiguration(PTMPConfiguration configuration) {
		try {
			dataOutputStream.writeInt(configuration.encoding());
			dataOutputStream.writeInt(configuration.encryption());
			dataOutputStream.writeInt(configuration.compression());
			dataOutputStream.writeInt(configuration.authentication());
		} catch (IOException e) {}
	}
	
	public void writeObject(Object object) {
		try {
			dataOutputStream.writeObject(object);
		} catch (IOException e) {}
	}
	public void writeObjects(Object... data) {
		try {
			dataOutputStream.writeObjects(data);
		} catch (IOException e) {}
	}
	
	
	public void reset() {
		outputStream = new ByteArrayOutputStream();
		dataOutputStream = new PTMPDataOutputStream(outputStream, encoding);
	}
	
}