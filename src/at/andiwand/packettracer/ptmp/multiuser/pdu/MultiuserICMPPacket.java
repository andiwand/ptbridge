package at.andiwand.packettracer.ptmp.multiuser.pdu;

import at.andiwand.packettracer.ptmp.PTMPDataReader;
import at.andiwand.packettracer.ptmp.PTMPDataWriter;


public class MultiuserICMPPacket extends MultiuserProtocolDataUnit {
	
	private byte type;
	private byte code;
	private int identifier;
	private int sequenceNumber;
	
	
	
	public byte getType() {
		return type;
	}
	public byte getCode() {
		return code;
	}
	public int getIdentifier() {
		return identifier;
	}
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	public void getBytes(PTMPDataWriter writer) {
		writer.writeByte(type);
		writer.writeByte(code);
		writer.writeInt(identifier);
		writer.writeInt(sequenceNumber);
	}
	
	public void setType(byte type) {
		this.type = type;
	}
	public void setCode(byte code) {
		this.code = code;
	}
	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	
	
	public void parse(PTMPDataReader reader) {
		type = reader.readByte();
		code = reader.readByte();
		identifier = reader.readInt();
		sequenceNumber = reader.readInt();
	}
	
}