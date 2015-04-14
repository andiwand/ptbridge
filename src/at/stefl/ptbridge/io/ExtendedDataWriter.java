package at.stefl.ptbridge.io;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.util.UUID;

import at.stefl.commons.network.mac.MACAddress;
import at.stefl.commons.util.UUIDUtil;


public class ExtendedDataWriter {
	
	private ByteArrayOutputStream outputStream;
	private DataOutputStream dataOutputStream;
	
	public ExtendedDataWriter() {
		reset();
	}
	
	public byte[] getData() {
		return outputStream.toByteArray();
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
	
	public void writeIP4Addres(Inet4Address ip4) {
		try {
			dataOutputStream.write(ip4.getAddress());
		} catch (IOException e) {}
	}
	
	public void writeIP6Addres(Inet6Address ip6) {
		try {
			dataOutputStream.write(ip6.getAddress());
		} catch (IOException e) {}
	}
	
	public void writeMACAddress(MACAddress macAddress) {
		try {
			dataOutputStream.write(macAddress.toByteArray());
		} catch (IOException e) {}
	}
	
	public void writeUUID(UUID uuid) {
		try {
			dataOutputStream.write(UUIDUtil.uuidToBytes(uuid));
		} catch (IOException e) {}
	}
	
	public int size() {
		return dataOutputStream.size();
	}
	
	public void reset() {
		outputStream = new ByteArrayOutputStream();
		dataOutputStream = new DataOutputStream(outputStream);
	}
	
}