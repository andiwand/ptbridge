package at.stefl.ptbridge.ptmp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;

import at.stefl.commons.network.ip.IPv4Address;
import at.stefl.commons.network.mac.MACAddress;


public class PTMPDataWriter extends OutputStream {
	
	private PTMPEncoding encoding;
	
	private ByteArrayOutputStream outputStream;
	private PTMPDataOutputStream dataOutputStream;
	
	public PTMPDataWriter(PTMPEncoding encoding) {
		this.encoding = encoding;
		
		reset();
	}
	
	public byte[] getData() {
		return outputStream.toByteArray();
	}
	
	public PTMPEncoding getEncoding() {
		return encoding;
	}
	
	public void write(int data) {
		try {
			dataOutputStream.write(data);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void write(byte[] data) {
		try {
			dataOutputStream.write(data);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void write(byte[] data, int offset, int length) {
		try {
			dataOutputStream.write(data, offset, length);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeByte(int b) {
		try {
			dataOutputStream.writeByte(b);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeBoolean(boolean b) {
		try {
			dataOutputStream.writeBoolean(b);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeShort(int s) {
		try {
			dataOutputStream.writeShort(s);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeInt(int i) {
		try {
			dataOutputStream.writeInt(i);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeLong(long l) {
		try {
			dataOutputStream.writeLong(l);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeFloat(float f) {
		try {
			dataOutputStream.writeFloat(f);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeDouble(double d) {
		try {
			dataOutputStream.writeDouble(d);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeString(String string) {
		try {
			dataOutputStream.writeString(string);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeIPv4Address(IPv4Address address) {
		try {
			dataOutputStream.writeIPv4Address(address);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeMACAddress(MACAddress macAddress) {
		try {
			dataOutputStream.writeMACAddress(macAddress);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeUuid(UUID uuid) {
		try {
			dataOutputStream.writeUuid(uuid);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeTimestamp(Date timestamp) {
		writeString(PTMPAssignments.TIMESTAMP_FORMAT.format(timestamp));
	}
	
	public void writeObject(Object object) {
		try {
			dataOutputStream.writeObject(object);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeObjects(Object... data) {
		try {
			dataOutputStream.writeObjects(data);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void reset() {
		outputStream = new ByteArrayOutputStream();
		dataOutputStream = new PTMPDataOutputStream(outputStream, encoding);
	}
	
}