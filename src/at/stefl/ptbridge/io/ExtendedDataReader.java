package at.stefl.ptbridge.io;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.util.UUID;

import at.stefl.commons.network.mac.MACAddress;
import at.stefl.commons.util.UUIDUtil;


public class ExtendedDataReader {
	
	private final byte[] data;
	private final int offset;
	private final int length;
	
	private ByteArrayInputStream inputStream;
	private DataInputStream dataInputStream;
	
	public ExtendedDataReader(byte[] data) {
		this.data = data;
		offset = 0;
		length = data.length;
		
		reset();
	}
	
	public ExtendedDataReader(byte[] data, int offset, int length) {
		this.data = data;
		this.offset = offset;
		this.length = length;
		
		reset();
	}
	
	public byte[] getData() {
		return data;
	}
	
	public int read() {
		try {
			return dataInputStream.read();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public int read(byte[] data) {
		try {
			return dataInputStream.read(data);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public int read(byte[] data, int offset, int length) {
		try {
			return dataInputStream.read(data, offset, length);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public byte readByte() {
		try {
			return dataInputStream.readByte();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public int readUnsignedByte() {
		try {
			return dataInputStream.readUnsignedByte();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public boolean readBoolean() {
		try {
			return dataInputStream.readBoolean();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public short readShort() {
		try {
			return dataInputStream.readShort();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public int readUnsignedShort() {
		try {
			return dataInputStream.readUnsignedShort();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public int readInt() {
		try {
			return dataInputStream.readInt();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public long readLong() {
		try {
			return dataInputStream.readLong();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public float readFloat() {
		try {
			return dataInputStream.readFloat();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public double readDouble() {
		try {
			return dataInputStream.readDouble();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public Inet4Address readIP4Addres() {
		try {
			byte[] tmp = new byte[4];
			dataInputStream.read(tmp);
			return (Inet4Address) Inet4Address.getByAddress(tmp);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public Inet6Address readIP6Addres() {
		try {
			byte[] tmp = new byte[16];
			dataInputStream.read(tmp);
			return (Inet6Address) Inet6Address.getByAddress(tmp);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public MACAddress readMACAddress() {
		try {
			byte[] tmp = new byte[6];
			dataInputStream.read(tmp);
			return new MACAddress(tmp);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public UUID readUUID() {
		try {
			byte[] tmp = new byte[16];
			dataInputStream.read(tmp);
			return UUIDUtil.bytesToUuid(tmp);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public void reset() {
		inputStream = new ByteArrayInputStream(data, offset, length);
		dataInputStream = new DataInputStream(inputStream);
	}
	
}