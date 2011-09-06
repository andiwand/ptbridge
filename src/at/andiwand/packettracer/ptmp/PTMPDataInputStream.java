package at.andiwand.packettracer.ptmp;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.text.ParseException;
import java.util.UUID;

import at.andiwand.library.network.mac.MACAddress;
import at.andiwand.library.network.mac.MACAddressFormat;
import at.andiwand.library.network.mac.SimpleMACAddressFormat;
import at.andiwand.library.steam.CountingInputStream;
import at.andiwand.library.util.UUIDUtil;


public class PTMPDataInputStream extends FilterInputStream {
	
	public static final int BYTE_SIZE	= 1;
	public static final int BOOL_SIZE	= 1;
	public static final int SHORT_SIZE	= 2;
	public static final int INT_SIZE	= 4;
	public static final int LONG_SIZE	= 8;
	public static final int FLOAT_SIZE	= 4;
	public static final int DOUBLE_SIZE	= 8;
	
	public static final char STRING_TERMINATION	= '\0';
	
	public static final int IP4_SIZE = 4;
	public static final int IP6_SIZE = 16;
	
	public static final int MAC_SIZE = MACAddress.SIZE;
	public static final MACAddressFormat MAC_FORMAT = new SimpleMACAddressFormat(
			"xxxx.xxxx.xxxx");
	
	public static final int UUID_SIZE = UUIDUtil.UUID_SIZE;
	
	
	
	
	private CountingInputStream countingInputStream;
	private DataInputStream dataInputStream;
	
	private int encoding;
	
	
	
	public PTMPDataInputStream(InputStream in) {
		this(in, PTMPConfiguration.DEFAULT_ENCODING);
	}
	public PTMPDataInputStream(InputStream in, int encoding) {
		super(new CountingInputStream(in));
		
		countingInputStream = (CountingInputStream) this.in;
		dataInputStream = new DataInputStream(countingInputStream);
		
		setEncoding(encoding);
	}
	public PTMPDataInputStream(InputStream in, PTMPConfiguration configuration) {
		this(in, configuration.encoding());
	}
	
	
	
	public int encoding() {
		return encoding;
	}
	public int streamedBytes() {
		return countingInputStream.count();
	}
	
	public void setEncoding(int encoding) {
		if (!PTMPConfiguration.legalEncoding(encoding))
			throw new RuntimeException("Unknown encoding: " + encoding);
		
		this.encoding = encoding;
	}
	public void setEncoding(PTMPConfiguration configuration) {
		setEncoding(configuration.encoding());
	}
	
	
	public byte readByte() throws IOException {
		switch (encoding) {
		case PTMPConfiguration.ENCODING_TEXT:
			String byteString = readString();
			return Byte.parseByte(byteString);
		case PTMPConfiguration.ENCODING_BINARY:
			return dataInputStream.readByte();
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	public boolean readBoolean() throws IOException {
		switch (encoding) {
		case PTMPConfiguration.ENCODING_TEXT:
			String boolString = readString();
			return Boolean.parseBoolean(boolString);
		case PTMPConfiguration.ENCODING_BINARY:
			return dataInputStream.readBoolean();
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	public short readShort() throws IOException {
		switch (encoding) {
		case PTMPConfiguration.ENCODING_TEXT:
			String shortString = readString();
			return Short.parseShort(shortString);
		case PTMPConfiguration.ENCODING_BINARY:
			return dataInputStream.readShort();
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	public int readInt() throws IOException {
		switch (encoding) {
		case PTMPConfiguration.ENCODING_TEXT:
			String intString = readString();
			return Integer.parseInt(intString);
		case PTMPConfiguration.ENCODING_BINARY:
			return dataInputStream.readInt();
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	public long readLong() throws IOException {
		switch (encoding) {
		case PTMPConfiguration.ENCODING_TEXT:
			String longString = readString();
			return Long.parseLong(longString);
		case PTMPConfiguration.ENCODING_BINARY:
			return dataInputStream.readLong();
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	public float readFloat() throws IOException {
		switch (encoding) {
		case PTMPConfiguration.ENCODING_TEXT:
			String floatString = readString();
			return Float.parseFloat(floatString);
		case PTMPConfiguration.ENCODING_BINARY:
			return dataInputStream.readFloat();
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	public double readDouble() throws IOException {
		switch (encoding) {
		case PTMPConfiguration.ENCODING_TEXT:
			String doubleString = readString();
			return Double.parseDouble(doubleString);
		case PTMPConfiguration.ENCODING_BINARY:
			return dataInputStream.readDouble();
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public String readString() throws IOException {
		StringBuilder builder = new StringBuilder();
		int read;
		
		while (true) {
			read = in.read();
			if (read == -1) throw new EOFException();
			if (read == STRING_TERMINATION) break;
			builder.appendCodePoint(read);
		}
		
		return builder.toString();
	}
	
	public Inet4Address readIP4Addres() throws IOException {
		switch (encoding) {
		case PTMPConfiguration.ENCODING_TEXT:
			String ip4String = readString();
			return (Inet4Address) Inet4Address.getByName(ip4String);
		case PTMPConfiguration.ENCODING_BINARY:
			byte[] ip4bytes = new byte[IP4_SIZE];
			read(ip4bytes);
			return (Inet4Address) Inet4Address.getByAddress(ip4bytes);
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	public Inet6Address readIP6Addres() throws IOException {
		switch (encoding) {
		case PTMPConfiguration.ENCODING_TEXT:
			String ip6String = readString();
			return (Inet6Address) Inet6Address.getByName(ip6String);
		case PTMPConfiguration.ENCODING_BINARY:
			byte[] ip6bytes = new byte[IP6_SIZE];
			read(ip6bytes);
			return (Inet6Address) Inet6Address.getByAddress(ip6bytes);
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public MACAddress readMACAddress() throws IOException {
		switch (encoding) {
		case PTMPConfiguration.ENCODING_TEXT:
			String macString = readString();
			
			try {
				return MAC_FORMAT.parseObject(macString);
			} catch (ParseException e) {
				return null;
			}
		case PTMPConfiguration.ENCODING_BINARY:
			byte[] macBytes = new byte[MAC_SIZE];
			read(macBytes);
			return MACAddress.getByAddress(macBytes);
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public UUID readUuid() throws IOException {
		switch (encoding) {
		case PTMPConfiguration.ENCODING_TEXT:
			String uuidString = readString();
			uuidString = uuidString.substring(1, uuidString.length() - 1);
			return UUID.fromString(uuidString);
		case PTMPConfiguration.ENCODING_BINARY:
			byte[] uuidBytes = new byte[UUID_SIZE];
			read(uuidBytes);
			return UUIDUtil.bytesToUuid(uuidBytes);
		
		default:
			throw new IllegalStateException("Unreachable section");
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
		} else if (clazz.equals(Inet4Address.class)) {
			return readIP4Addres();
		} else if (clazz.equals(Inet6Address.class)) {
			return readIP6Addres();
		} else if (clazz.equals(MACAddress.class)) {
			return readMACAddress();
		} else if (clazz.equals(UUID.class)) {
			return readUuid();
		} else {
			throw new IllegalArgumentException("Unsupported argument");
		}
	}
	public Object[] readObjects(Class<?>... definition) throws IOException {
		Object[] result = new Object[definition.length];
		
		for (int i = 0; i < definition.length; i++)
			result[i] = readObject(definition[i]);
		
		return result;
	}
	public void readObjects(Object[] data, Class<?>... definition) throws IOException {
		for (int i = 0; i < definition.length; i++)
			data[i] = readObject(definition[i]);
	}
	
	
	public int skipBytes(int n) throws IOException {
		return dataInputStream.skipBytes(n);
	}
	
}