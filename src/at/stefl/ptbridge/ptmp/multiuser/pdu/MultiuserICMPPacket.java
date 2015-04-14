package at.stefl.ptbridge.ptmp.multiuser.pdu;

import java.util.HashMap;
import java.util.Map;

import at.stefl.commons.network.Assignments;
import at.stefl.ptbridge.ptmp.PTMPDataReader;
import at.stefl.ptbridge.ptmp.PTMPDataWriter;


/*
 * CIcmpHeader binary | byte | byte | short | short | short | logical | type |
 * code | checksum | identifier | sequence number |
 */
public class MultiuserICMPPacket extends MultiuserPDU {
	
	public abstract static class ICMPPayload extends MultiuserPDU {}
	
	public static class EchoPayload extends ICMPPayload {
		private int identifier;
		private int sequenceNumber;
		
		public int getIdentifier() {
			return identifier;
		}
		
		public int getSequenceNumber() {
			return sequenceNumber;
		}
		
		public void getBytes(PTMPDataWriter writer) {
			writer.writeInt(identifier);
			writer.writeInt(sequenceNumber);
		}
		
		public void setIdentifier(int identifier) {
			this.identifier = identifier;
		}
		
		public void setSequenceNumber(int sequenceNumber) {
			this.sequenceNumber = sequenceNumber;
		}
		
		public void parse(PTMPDataReader reader) {
			identifier = reader.readInt();
			sequenceNumber = reader.readInt();
		}
	}
	
	private static final Map<Byte, Class<? extends ICMPPayload>> PAYLOAD_CLASS_MAP = new HashMap<Byte, Class<? extends ICMPPayload>>();
	
	static {
		PAYLOAD_CLASS_MAP.put(Assignments.ICMP.TYPE_ECHO, EchoPayload.class);
		PAYLOAD_CLASS_MAP.put(Assignments.ICMP.TYPE_ECHO_REPLY,
				EchoPayload.class);
	}
	
	private static ICMPPayload getPayloadInstance(byte type) {
		Class<? extends ICMPPayload> clazz = PAYLOAD_CLASS_MAP.get(type);
		
		if (clazz == null) throw new IllegalArgumentException("Unknown type!");
		
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot create instace!");
		}
	}
	
	private byte type;
	private byte code;
	private ICMPPayload payload;
	
	public byte getType() {
		return type;
	}
	
	public byte getCode() {
		return code;
	}
	
	public ICMPPayload getPayload() {
		return payload;
	}
	
	public void getBytes(PTMPDataWriter writer) {
		writer.writeString("");
		
		writer.writeByte(type);
		writer.writeByte(code);
		writer.writeShort(0); // checksum
		
		payload.getBytes(writer);
	}
	
	public void setType(byte type) {
		this.type = type;
	}
	
	public void setCode(byte code) {
		this.code = code;
	}
	
	public void setPayload(ICMPPayload payload) {
		this.payload = payload;
	}
	
	public void parse(PTMPDataReader reader) {
		MultiuserVariableSizePDUKiller.kill(reader);
		
		type = reader.readByte();
		code = reader.readByte();
		reader.readShort(); // checksum
		
		payload = getPayloadInstance(type);
		payload.parse(reader);
	}
}