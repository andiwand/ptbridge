package at.andiwand.packettracer.bridge.ptmp.multiuser.pdu;

import at.andiwand.packettracer.bridge.ptmp.PTMPDataReader;
import at.andiwand.packettracer.bridge.ptmp.PTMPDataWriter;


public class MultiuserTCPSegment extends MultiuserPDU {
	
	private static final MultiuserPayloadAssociator PAYLOAD_ASSOCIATOR = new MultiuserPayloadAssociator();
	
	static {
		PAYLOAD_ASSOCIATOR.putEntry("CTelnetPacket",
				MultiuserTelnetSegment.class);
	}
	
	private MultiuserPDU payload;
	private short sourcePort;
	private short destinationPort;
	private int unknown2;
	private int sequenceNumber;
	private int acknowledgmentNumber;
	private byte unknown3;
	private byte unknown4;
	private byte flags;
	private short unknown5;
	private short unknown6;
	private short unknown7;
	private int unknown8;
	private int unknown9;
	
	public MultiuserPDU getPayload() {
		return payload;
	}
	
	public short getSourcePort() {
		return sourcePort;
	}
	
	public short getDestinationPort() {
		return destinationPort;
	}
	
	public int getUnknown2() {
		return unknown2;
	}
	
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	
	public int getAcknowledgmentNumber() {
		return acknowledgmentNumber;
	}
	
	public byte getUnknown3() {
		return unknown3;
	}
	
	public byte getUnknown4() {
		return unknown4;
	}
	
	public byte getFlags() {
		return flags;
	}
	
	public short getUnknown5() {
		return unknown5;
	}
	
	public short getUnknown6() {
		return unknown6;
	}
	
	public short getUnknown7() {
		return unknown7;
	}
	
	public int getUnknown8() {
		return unknown8;
	}
	
	public int getUnknown9() {
		return unknown9;
	}
	
	public void getBytes(PTMPDataWriter writer) {
		Class<? extends MultiuserPDU> payloadClass = payload.getClass();
		String payloadName = PAYLOAD_ASSOCIATOR.getPayloadName(payloadClass);
		writer.writeString(payloadName);
		payload.getBytes(writer);
		
		writer.writeShort(sourcePort);
		writer.writeShort(destinationPort);
		writer.writeInt(unknown2);
		writer.writeInt(sequenceNumber);
		writer.writeInt(acknowledgmentNumber);
		writer.writeByte(unknown3);
		writer.writeByte(unknown4);
		writer.writeByte(flags);
		writer.writeShort(unknown5);
		writer.writeShort(unknown6);
		writer.writeShort(unknown7);
		writer.writeInt(unknown8);
		writer.writeInt(unknown9);
		
		if (unknown9 == 1) {
			writer.writeString("CTcpOptionMSS");
			writer.writeInt(2);
			writer.writeInt(4);
			writer.writeInt(1460);
		}
	}
	
	public void setPayload(MultiuserPDU payload) {
		this.payload = payload;
	}
	
	public void setSourcePort(short sourcePort) {
		this.sourcePort = sourcePort;
	}
	
	public void setDestinationPort(short destinationPort) {
		this.destinationPort = destinationPort;
	}
	
	public void setUnknown2(int unknown2) {
		this.unknown2 = unknown2;
	}
	
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	
	public void setAcknowledgmentNumber(int acknowledgmentNumber) {
		this.acknowledgmentNumber = acknowledgmentNumber;
	}
	
	public void setUnknown3(byte unknown3) {
		this.unknown3 = unknown3;
	}
	
	public void setUnknown4(byte unknown4) {
		this.unknown4 = unknown4;
	}
	
	public void setFlags(byte flags) {
		this.flags = flags;
	}
	
	public void setUnknown5(short unknown5) {
		this.unknown5 = unknown5;
	}
	
	public void setUnknown6(short unknown6) {
		this.unknown6 = unknown6;
	}
	
	public void setUnknown7(short unknown7) {
		this.unknown7 = unknown7;
	}
	
	public void setUnknown8(int unknown8) {
		this.unknown8 = unknown8;
	}
	
	public void setUnknown9(int unknown9) {
		this.unknown9 = unknown9;
	}
	
	public void parse(PTMPDataReader reader) {
		String payloadName = reader.readString();
		
		if (!payloadName.isEmpty()) {
			payload = PAYLOAD_ASSOCIATOR.getPayloadInstance(payloadName);
			payload.parse(reader);
		}
		
		sourcePort = reader.readShort();
		destinationPort = reader.readShort();
		unknown2 = reader.readInt();
		sequenceNumber = reader.readInt();
		acknowledgmentNumber = reader.readInt();
		unknown3 = reader.readByte();
		unknown4 = reader.readByte();
		flags = reader.readByte();
		unknown5 = reader.readShort();
		unknown6 = reader.readShort();
		unknown7 = reader.readShort();
		unknown8 = reader.readInt();
		unknown9 = reader.readInt();
		
		if (unknown9 == 1) {
			reader.readString();
			reader.readInt();
			reader.readInt();
			reader.readInt();
		}
	}
	
}