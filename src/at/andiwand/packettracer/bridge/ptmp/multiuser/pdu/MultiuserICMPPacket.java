package at.andiwand.packettracer.bridge.ptmp.multiuser.pdu;

import at.andiwand.packettracer.bridge.ptmp.PTMPDataReader;
import at.andiwand.packettracer.bridge.ptmp.PTMPDataWriter;


public abstract class MultiuserICMPPacket extends MultiuserPDU {
	
	public static class Echo extends MultiuserICMPPacket {
		private int identifier;
		private int sequenceNumber;
		
		public int getIdentifier() {
			return identifier;
		}
		
		public int getSequenceNumber() {
			return sequenceNumber;
		}
		
		public void getBytes(PTMPDataWriter writer) {
			super.getBytes(writer);
			
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
			super.parse(reader);
			
			identifier = reader.readInt();
			sequenceNumber = reader.readInt();
		}
	}
	
	private byte type;
	private byte code;
	
	public byte getType() {
		return type;
	}
	
	public byte getCode() {
		return code;
	}
	
	public void getBytes(PTMPDataWriter writer) {
		writer.writeByte(type);
		writer.writeByte(code);
	}
	
	public void setType(byte type) {
		this.type = type;
	}
	
	public void setCode(byte code) {
		this.code = code;
	}
	
	public void parse(PTMPDataReader reader) {
		type = reader.readByte();
		code = reader.readByte();
	}
	
}