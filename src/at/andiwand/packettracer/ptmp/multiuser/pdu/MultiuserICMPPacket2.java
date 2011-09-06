package at.andiwand.packettracer.ptmp.multiuser.pdu;

import at.andiwand.library.network.Assignments;
import at.andiwand.packettracer.ptmp.PTMPDataReader;
import at.andiwand.packettracer.ptmp.PTMPDataWriter;


public class MultiuserICMPPacket2 extends MultiuserProtocolDataUnit {
	
	public static abstract class Payload extends MultiuserProtocolDataUnit {
		public static class Echo extends Payload {
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
	}
	
	
	
	
	private byte type;
	private byte code;
	private Payload payload;
	
	
	
	public byte getType() {
		return type;
	}
	public byte getCode() {
		return code;
	}
	public Payload getPayload() {
		return payload;
	}
	public void getBytes(PTMPDataWriter writer) {
		writer.writeByte(type);
		writer.writeByte(code);
		
		payload.getBytes(writer);
	}
	
	public void setType(byte type) {
		this.type = type;
	}
	public void setCode(byte code) {
		this.code = code;
	}
	public void setPayload(Payload payload) {
		this.payload = payload;
	}
	
	
	public void parse(PTMPDataReader reader) {
		type = reader.readByte();
		code = reader.readByte();
		
		switch (type) {
		case Assignments.ICMP.TYPE_ECHO:
		case Assignments.ICMP.TYPE_ECHO_REPLY:
			payload = new Payload.Echo();
			break;
		}
		
		payload.parse(reader);
	}
	
}