package at.andiwand.packettracer.ptmp.packet;

import at.andiwand.packettracer.ptmp.PTMPConfiguration;
import at.andiwand.packettracer.ptmp.PTMPDataReader;
import at.andiwand.packettracer.ptmp.PTMPDataWriter;


public abstract class PTMPPacket {
	
	public static final int TYPE_NEGOTIATION_REQUEST		= 0;
	public static final int TYPE_NEGOTIATION_RESPONSE		= 1;
	public static final int TYPE_AUTHENTICATION_REQUEST		= 2;
	public static final int TYPE_AUTHENTICATION_CHALLENGE	= 3;
	public static final int TYPE_AUTHENTICATION_RESPONSE	= 4;
	public static final int TYPE_AUTHENTICATION_STATUS		= 5;
	public static final int TYPE_KEEPALIVE					= 6;
	public static final int TYPE_DISCONNECT					= 7;
	public static final int TYPE_IPC_MESSAGES_MIN			= 100;
	public static final int TYPE_IPC_MESSAGES_MAX			= 199;
	public static final int TYPE_MULTIUSER_MESSAGES_MIN		= 200;
	public static final int TYPE_MULTIUSER_MESSAGES_MAX		= 299;
	
	
	
	private final int type;
	
	
	public PTMPPacket(int type) {
		if (!legalType(type))
			throw new IllegalArgumentException("Illegal type");
		
		this.type = type;
	}
	public PTMPPacket(PTMPDataReader reader) {
		this(reader.readInt());
		
		parseValue(reader);
	}
	public PTMPPacket(byte[] packet, int encoding) {
		this(new PTMPDataReader(packet, encoding));
	}
	public PTMPPacket(PTMPEncodedPacket packet) {
		this(packet.getType());
		
		parseValue(new PTMPDataReader(packet.getValue(), packet.encoding()));
	}
	public PTMPPacket(PTMPPacket packet) {
		this(packet.type);
	}
	
	
	public String toString() {
		return "type: " + type;
	}
	
	public int getType() {
		return type;
	}
	public abstract void getValue(PTMPDataWriter writer);
	public byte[] getValue(int encoding) {
		PTMPDataWriter writer = new PTMPDataWriter(encoding);
		getValue(writer);
		return writer.getData();
	}
	public byte[] getValue(PTMPConfiguration configuration) {
		return getValue(configuration.encoding());
	}
	public void getBytes(PTMPDataWriter writer) {
		writer.writeInt(type);
		getValue(writer);
	}
	public byte[] getBytes(int encoding) {
		PTMPDataWriter writer = new PTMPDataWriter(encoding);
		getBytes(writer);
		return writer.getData();
	}
	public byte[] getBytes(PTMPConfiguration configuration) {
		return getBytes(configuration.encoding());
	}
	
	public abstract void parseValue(PTMPDataReader reader);
	public void parseValue(byte[] value, int encoding) {
		parseValue(new PTMPDataReader(value, encoding));
	}
	public void parseValue(PTMPEncodedPacket packet) {
		parseValue(packet.getValue(), packet.encoding());
	}
	
	protected abstract boolean legalType(int type);
	
}