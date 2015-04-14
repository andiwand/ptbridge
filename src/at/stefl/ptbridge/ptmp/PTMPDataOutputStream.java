package at.stefl.ptbridge.ptmp;

import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import at.stefl.commons.network.ip.IPv4Address;
import at.stefl.commons.network.mac.MACAddress;
import at.stefl.commons.util.UUIDUtil;


public class PTMPDataOutputStream extends FilterOutputStream {
	
	private DataOutputStream dataOutputStream;
	
	private PTMPEncoding encoding;
	
	public PTMPDataOutputStream(OutputStream out) {
		this(out, PTMPConfiguration.DEFAULT_ENCODING);
	}
	
	public PTMPDataOutputStream(OutputStream out, PTMPEncoding encoding) {
		super(new DataOutputStream(out));
		
		dataOutputStream = (DataOutputStream) this.out;
		setEncoding(encoding);
	}
	
	public PTMPDataOutputStream(OutputStream out,
			PTMPConfiguration configuration) {
		this(out, configuration.getEncoding());
	}
	
	public PTMPEncoding getEncoding() {
		return encoding;
	}
	
	public int streamedBytes() {
		return dataOutputStream.size();
	}
	
	public void setEncoding(PTMPEncoding encoding) {
		this.encoding = encoding;
	}
	
	public void setEncoding(PTMPConfiguration configuration) {
		setEncoding(configuration.getEncoding());
	}
	
	public void writeByte(int b) throws IOException {
		switch (encoding) {
		case TEXT:
			writeString(b + "");
			break;
		case BINARY:
			dataOutputStream.writeByte(b);
			break;
		default:
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeBoolean(boolean b) throws IOException {
		switch (encoding) {
		case TEXT:
			writeString(b + "");
			break;
		case BINARY:
			dataOutputStream.writeBoolean(b);
			break;
		default:
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeShort(int s) throws IOException {
		switch (encoding) {
		case TEXT:
			writeString(s + "");
			break;
		case BINARY:
			dataOutputStream.writeShort(s);
			break;
		default:
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeInt(int i) throws IOException {
		switch (encoding) {
		case TEXT:
			writeString(i + "");
			break;
		case BINARY:
			dataOutputStream.writeInt(i);
			break;
		default:
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeLong(long l) throws IOException {
		switch (encoding) {
		case TEXT:
			writeString(l + "");
			break;
		case BINARY:
			dataOutputStream.writeLong(l);
			break;
		default:
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeFloat(float f) throws IOException {
		switch (encoding) {
		case TEXT:
			writeString(f + "");
			break;
		case BINARY:
			dataOutputStream.writeFloat(f);
			break;
		default:
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeDouble(double d) throws IOException {
		switch (encoding) {
		case TEXT:
			writeString(d + "");
			break;
		case BINARY:
			dataOutputStream.writeDouble(d);
			break;
		default:
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeString(String string) throws IOException {
		out.write((string + PTMPAssignments.STRING_TERMINATION)
				.getBytes(PTMPAssignments.STRING_CHARSET));
	}
	
	public void writeIPv4Address(IPv4Address address) throws IOException {
		switch (encoding) {
		case TEXT:
			writeString(address.toDottedString());
			break;
		case BINARY:
			out.write(address.toByteArray());
			break;
		default:
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeMACAddress(MACAddress address) throws IOException {
		switch (encoding) {
		case TEXT:
			writeString(PTMPAssignments.MAC_ADDRESS_FORMAT.format(address));
			break;
		case BINARY:
			out.write(address.toByteArray());
			break;
		default:
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeUuid(UUID uuid) throws IOException {
		switch (encoding) {
		case TEXT:
			writeString("{" + uuid.toString() + "}");
			break;
		case BINARY:
			byte[] uuidBytes = UUIDUtil.uuidToBytes(uuid);
			write(uuidBytes);
			break;
		default:
			throw new IllegalStateException("Unreachable section!");
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
		} else if (object.getClass().equals(IPv4Address.class)) {
			writeIPv4Address((IPv4Address) object);
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