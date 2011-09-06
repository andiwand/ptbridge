package at.andiwand.packettracer.ptmp;

import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.nio.charset.Charset;
import java.util.UUID;

import at.andiwand.library.network.mac.MACAddress;
import at.andiwand.library.network.mac.MACAddressFormat;
import at.andiwand.library.network.mac.SimpleMACAddressFormat;
import at.andiwand.library.util.UUIDUtil;


public class PTMPDataOutputStream extends FilterOutputStream {
	
	public static final char STRING_TERMINATION	= '\0';
	public static final Charset STRING_CHARSET	= Charset.forName("utf-8");
	
	public static final MACAddressFormat MAC_FORMAT = new SimpleMACAddressFormat(
			"XXXX.XXXX.XXXX");
	
	
	
	
	private DataOutputStream dataOutputStream;
	
	private int encoding;
	
	
	
	public PTMPDataOutputStream(OutputStream out) {
		this(out, PTMPConfiguration.DEFAULT_ENCODING);
	}
	public PTMPDataOutputStream(OutputStream out, int encoding) {
		super(new DataOutputStream(out));
		
		dataOutputStream = (DataOutputStream) this.out;
		
		setEncoding(encoding);
	}
	public PTMPDataOutputStream(OutputStream out,
			PTMPConfiguration configuration) {
		this(out, configuration.encoding());
	}
	
	
	
	public int getEncoding() {
		return encoding;
	}
	public int streamedBytes() {
		return dataOutputStream.size();
	}
	
	public void setEncoding(int encoding) {
		if (!PTMPConfiguration.legalEncoding(encoding))
			throw new RuntimeException("Unknown encoding: " + encoding);
		
		this.encoding = encoding;
	}
	public void setEncoding(PTMPConfiguration configuration) {
		setEncoding(configuration.encoding());
	}
	
	
	public void writeByte(int b) throws IOException {
		switch (encoding) {
		case PTMPConfiguration.ENCODING_TEXT:
			String byteString = b + "";
			writeString(byteString);
			break;
		case PTMPConfiguration.ENCODING_BINARY:
			dataOutputStream.writeByte(b);
			break;
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	public void writeBoolean(boolean b) throws IOException {
		switch (encoding) {
		case PTMPConfiguration.ENCODING_TEXT:
			String boolString = b + "";
			writeString(boolString);
			break;
		case PTMPConfiguration.ENCODING_BINARY:
			dataOutputStream.writeBoolean(b);
			break;
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	public void writeShort(int s) throws IOException {
		switch (encoding) {
		case PTMPConfiguration.ENCODING_TEXT:
			String shortString = s + "";
			writeString(shortString);
			break;
		case PTMPConfiguration.ENCODING_BINARY:
			dataOutputStream.writeShort(s);
			break;
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	public void writeInt(int i) throws IOException {
		switch (encoding) {
		case PTMPConfiguration.ENCODING_TEXT:
			String intString = i + "";
			writeString(intString);
			break;
		case PTMPConfiguration.ENCODING_BINARY:
			dataOutputStream.writeInt(i);
			break;
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	public void writeLong(long l) throws IOException {
		switch (encoding) {
		case PTMPConfiguration.ENCODING_TEXT:
			String longString = l + "";
			writeString(longString);
			break;
		case PTMPConfiguration.ENCODING_BINARY:
			dataOutputStream.writeLong(l);
			break;
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	public void writeFloat(float f) throws IOException {
		switch (encoding) {
		case PTMPConfiguration.ENCODING_TEXT:
			String floatString = f + "";
			writeString(floatString);
			break;
		case PTMPConfiguration.ENCODING_BINARY:
			dataOutputStream.writeFloat(f);
			break;
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	public void writeDouble(double d) throws IOException {
		switch (encoding) {
		case PTMPConfiguration.ENCODING_TEXT:
			String doubleString = d + "";
			writeString(doubleString);
			break;
		case PTMPConfiguration.ENCODING_BINARY:
			dataOutputStream.writeDouble(d);
			break;
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public void writeString(String string) throws IOException {
		out.write((string + STRING_TERMINATION).getBytes(STRING_CHARSET));
	}
	
	public void writeIP4Addres(Inet4Address ip4) throws IOException {
		switch (encoding) {
		case PTMPConfiguration.ENCODING_TEXT:
			String ip4String = ip4.getHostAddress();
			writeString(ip4String);
			break;
		case PTMPConfiguration.ENCODING_BINARY:
			out.write(ip4.getAddress());
			break;
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	public void writeIP6Addres(Inet6Address ip6) throws IOException {
		switch (encoding) {
		case PTMPConfiguration.ENCODING_TEXT:
			String ip6String = ip6.getHostAddress();
			writeString(ip6String);
			break;
		case PTMPConfiguration.ENCODING_BINARY:
			out.write(ip6.getAddress());
			break;
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public void writeMACAddress(MACAddress macAddress) throws IOException {
		switch (encoding) {
		case PTMPConfiguration.ENCODING_TEXT:
			String macString = MAC_FORMAT.format(macAddress);
			writeString(macString);
			break;
		case PTMPConfiguration.ENCODING_BINARY:
			out.write(macAddress.getAddress());
			break;
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public void writeUuid(UUID uuid) throws IOException {
		switch (encoding) {
		case PTMPConfiguration.ENCODING_TEXT:
			String uuidString = "{" + uuid + "}";
			writeString(uuidString);
			break;
		case PTMPConfiguration.ENCODING_BINARY:
			byte[] uuidBytes = UUIDUtil.uuidToBytes(uuid);
			write(uuidBytes);
			break;
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public void writeObject(Object object) throws IOException {
		if (object.getClass().equals(Boolean.class)) {
			writeBoolean((Boolean) object);
		} else if (object.getClass().equals(Byte.class)) {
			writeByte((Byte) object);
		} else if (object.getClass().equals(Short.class)) {
			writeShort((Short) object);
		} else if (object.getClass().equals(Integer.class)) {
			writeInt((Integer) object);
		} else if (object.getClass().equals(Long.class)) {
			writeLong((Long) object);
		} else if (object.getClass().equals(Float.class)) {
			writeFloat((Float) object);
		} else if (object.getClass().equals(Double.class)) {
			writeDouble((Double) object);
		} else if (object.getClass().equals(String.class)) {
			writeString((String) object);
		} else if (object.getClass().equals(Inet4Address.class)) {
			writeIP4Addres((Inet4Address) object);
		} else if (object.getClass().equals(Inet6Address.class)) {
			writeIP6Addres((Inet6Address) object);
		} else if (object.getClass().equals(MACAddress.class)) {
			writeMACAddress((MACAddress) object);
		} else if (object.getClass().equals(UUID.class)) {
			writeUuid((UUID) object);
		} else if (object.getClass().equals(byte[].class)) {
			write((byte[]) object);
		} else {
			throw new IllegalArgumentException("Unsupported argument");
		}
	}
	public void writeObjects(Object... data) throws IOException {
		for (int i = 0; i < data.length; i++) {
			writeObject(data[i]);
		}
	}
	
	
	public void flush() throws IOException {
		out.flush();
	}
	
}