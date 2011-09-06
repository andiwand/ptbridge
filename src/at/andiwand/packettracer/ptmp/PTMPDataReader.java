package at.andiwand.packettracer.ptmp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.util.UUID;

import at.andiwand.library.network.mac.MACAddress;


public class PTMPDataReader extends InputStream {
	
	private final byte[] data;
	private final int offset;
	private final int length;
	private int encoding;
	
	private ByteArrayInputStream inputStream;
	private PTMPDataInputStream dataInputStream;
	
	
	
	public PTMPDataReader(byte[] data, int encoding) {
		this.data = data;
		offset = 0;
		length = data.length;
		this.encoding = encoding;
		
		reset();
	}
	public PTMPDataReader(byte[] data, int offset, int length, int encoding) {
		this.data = data;
		this.offset = offset;
		this.length = length;
		this.encoding = encoding;
		
		reset();
	}
	
	
	
	public byte[] getData() {
		return data;
	}
	public int encoding() {
		return encoding;
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
	
	public String readString() {
		try {
			return dataInputStream.readString();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public Inet4Address readIP4Addres() {
		try {
			return dataInputStream.readIP4Addres();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	public Inet6Address readIP6Addres() {
		try {
			return dataInputStream.readIP6Addres();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public MACAddress readMACAddress() {
		try {
			return dataInputStream.readMACAddress();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public UUID readUuid() {
		try {
			return dataInputStream.readUuid();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public PTMPConfiguration readConfiguration() {
		return new PTMPConfiguration(readInt(), readInt(), readInt(), readInt());
	}
	
	public Object readObject(Class<?> clazz) {
		try {
			return dataInputStream.readObject(clazz);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	public Object[] readObjects(Class<?>... definition) {
		try {
			return dataInputStream.readObjects(definition);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	public void readObjects(Object[] data, Class<?>... definition) {
		try {
			dataInputStream.readObjects(data, definition);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	
	public void reset() {
		inputStream = new ByteArrayInputStream(data, offset, length);
		dataInputStream = new PTMPDataInputStream(inputStream, encoding);
	}
	
}