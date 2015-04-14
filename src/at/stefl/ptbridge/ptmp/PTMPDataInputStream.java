package at.stefl.ptbridge.ptmp;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.UUID;

import at.stefl.commons.io.CountingInputStream;
import at.stefl.commons.network.ip.IPv4Address;
import at.stefl.commons.network.mac.MACAddress;
import at.stefl.commons.util.UUIDUtil;


public class PTMPDataInputStream extends FilterInputStream {
	
	private CountingInputStream countingInputStream;
	private DataInputStream dataInputStream;
	
	private PTMPEncoding encoding;
	
	public PTMPDataInputStream(InputStream in) {
		this(in, PTMPConfiguration.DEFAULT_ENCODING);
	}
	
	public PTMPDataInputStream(InputStream in, PTMPEncoding encoding) {
		super(new CountingInputStream(in));
		
		countingInputStream = (CountingInputStream) this.in;
		dataInputStream = new DataInputStream(countingInputStream);
		
		setEncoding(encoding);
	}
	
	public PTMPDataInputStream(InputStream in, PTMPConfiguration configuration) {
		this(in, configuration.getEncoding());
	}
	
	public PTMPEncoding getEncoding() {
		return encoding;
	}
	
	public long streamedBytes() {
		return countingInputStream.count();
	}
	
	public void setEncoding(PTMPEncoding encoding) {
		this.encoding = encoding;
	}
	
	public void setEncoding(PTMPConfiguration configuration) {
		setEncoding(configuration.getEncoding());
	}
	
	public byte readByte() throws IOException {
		switch (encoding) {
		case TEXT:
			return Byte.parseByte(readString());
		case BINARY:
			return dataInputStream.readByte();
		default:
			throw new IllegalStateException("Unsupported encoding!");
		}
	}
	
	public boolean readBoolean() throws IOException {
		switch (encoding) {
		case TEXT:
			return Boolean.parseBoolean(readString());
		case BINARY:
			return dataInputStream.readBoolean();
		default:
			throw new IllegalStateException("Unsupported encoding!");
		}
	}
	
	public short readShort() throws IOException {
		switch (encoding) {
		case TEXT:
			return Short.parseShort(readString());
		case BINARY:
			return dataInputStream.readShort();
		default:
			throw new IllegalStateException("Unsupported encoding!");
		}
	}
	
	public int readInt() throws IOException {
		switch (encoding) {
		case TEXT:
			return Integer.parseInt(readString());
		case BINARY:
			return dataInputStream.readInt();
		default:
			throw new IllegalStateException("Unsupported encoding!");
		}
	}
	
	public long readLong() throws IOException {
		switch (encoding) {
		case TEXT:
			return Long.parseLong(readString());
		case BINARY:
			return dataInputStream.readLong();
		default:
			throw new IllegalStateException("Unsupported encoding!");
		}
	}
	
	public float readFloat() throws IOException {
		switch (encoding) {
		case TEXT:
			return Float.parseFloat(readString());
		case BINARY:
			return dataInputStream.readFloat();
		default:
			throw new IllegalStateException("Unsupported encoding!");
		}
	}
	
	public double readDouble() throws IOException {
		switch (encoding) {
		case TEXT:
			return Double.parseDouble(readString());
		case BINARY:
			return dataInputStream.readDouble();
		default:
			throw new IllegalStateException("Unsupported encoding!");
		}
	}
	
	public String readString() throws IOException {
		StringBuilder builder = new StringBuilder();
		int read;
		
		while (true) {
			read = in.read();
			if (read == -1) throw new EOFException();
			if (read == PTMPAssignments.STRING_TERMINATION) break;
			builder.appendCodePoint(read);
		}
		
		return builder.toString();
	}
	
	public IPv4Address readIPv4Address() throws IOException {
		switch (encoding) {
		case TEXT:
			return new IPv4Address(readString());
		case BINARY:
			byte[] ip4bytes = new byte[PTMPAssignments.IP4_SIZE];
			read(ip4bytes);
			return new IPv4Address(ip4bytes);
		default:
			throw new IllegalStateException("Unsupported encoding!");
		}
	}
	
	// TODO: exception
	public MACAddress readMACAddress() throws IOException {
		switch (encoding) {
		case TEXT:
			try {
				return PTMPAssignments.MAC_ADDRESS_FORMAT
						.parseObject(readString());
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		case BINARY:
			byte[] macBytes = new byte[PTMPAssignments.MAC_SIZE];
			read(macBytes);
			return new MACAddress(macBytes);
		default:
			throw new IllegalStateException("Unsupported encoding!");
		}
	}
	
	public UUID readUuid() throws IOException {
		switch (encoding) {
		case TEXT:
			String uuidString = readString();
			uuidString = uuidString.substring(1, uuidString.length() - 1);
			return UUID.fromString(uuidString);
		case BINARY:
			byte[] uuidBytes = new byte[PTMPAssignments.UUID_SIZE];
			read(uuidBytes);
			return UUIDUtil.bytesToUuid(uuidBytes);
		default:
			throw new IllegalStateException("Unsupported encoding!");
		}
	}
	
	public Object readObject(Class<?> clazz) throws IOException {
		if (clazz.equals(Boolean.class)) {
			return readBoolean();
		} else if (clazz.equals(Byte.class)) {
			return readByte();
		} else if (clazz.equals(Short.class)) {
			return readShort();
		} else if (clazz.equals(Integer.class)) {
			return readInt();
		} else if (clazz.equals(Long.class)) {
			return readLong();
		} else if (clazz.equals(Float.class)) {
			return readFloat();
		} else if (clazz.equals(Double.class)) {
			return readDouble();
		} else if (clazz.equals(String.class)) {
			return readString();
		} else if (clazz.equals(IPv4Address.class)) {
			return readIPv4Address();
		} else if (clazz.equals(MACAddress.class)) {
			return readMACAddress();
		} else if (clazz.equals(UUID.class)) {
			return readUuid();
		} else {
			throw new IllegalArgumentException("Unsupported argument!");
		}
	}
	
	public Object[] readObjects(Class<?>... definition) throws IOException {
		Object[] result = new Object[definition.length];
		
		for (int i = 0; i < definition.length; i++)
			result[i] = readObject(definition[i]);
		
		return result;
	}
	
	public void readObjects(Object[] data, Class<?>... definition)
			throws IOException {
		for (int i = 0; i < definition.length; i++)
			data[i] = readObject(definition[i]);
	}
	
}