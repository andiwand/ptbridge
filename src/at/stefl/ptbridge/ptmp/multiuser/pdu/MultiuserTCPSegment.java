package at.stefl.ptbridge.ptmp.multiuser.pdu;

import at.stefl.ptbridge.ptmp.PTMPDataReader;
import at.stefl.ptbridge.ptmp.PTMPDataWriter;


public class MultiuserTCPSegment extends MultiuserPDU {
	
	private static final MultiuserPayloadAssociator PAYLOAD_ASSOCIATOR = new MultiuserPayloadAssociator();
	
	static {
		PAYLOAD_ASSOCIATOR.putEntry("TelnetPacket",
				MultiuserTelnetSegment.class);
	}
	
	private MultiuserPDU payload;
	private int sourcePort;
	private int destinationPort;
	private int unknown1;
	private long sequenceNumber;
	private long acknowledgmentNumber;
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
	
	public int getSourcePort() {
		return sourcePort;
	}
	
	public int getDestinationPort() {
		return destinationPort;
	}
	
	public int getUnknown2() {
		return unknown1;
	}
	
	public long getSequenceNumber() {
		return sequenceNumber;
	}
	
	public long getAcknowledgmentNumber() {
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
		if (payload != null) {
			Class<? extends MultiuserPDU> payloadClass = payload.getClass();
			String payloadName = PAYLOAD_ASSOCIATOR
					.getPayloadName(payloadClass);
			writer.writeString(payloadName);
			payload.getBytes(writer);
		} else {
			writer.writeString("");
		}
		
		writer.writeShort((short) sourcePort);
		writer.writeShort((short) destinationPort);
		writer.writeInt(unknown1);
		writer.writeInt((int) sequenceNumber);
		writer.writeInt((int) acknowledgmentNumber);
		writer.writeByte(unknown3);
		writer.writeByte(unknown4);
		writer.writeByte(flags);
		writer.writeShort(unknown5);
		writer.writeShort(unknown6);
		writer.writeShort(unknown7);
		
		if (unknown7 == 1) {
			writer.writeString("CTcpOptionMSS");
			writer.writeInt(2);
			writer.writeInt(4);
			writer.writeInt(1460);
		}
		
		writer.writeInt(unknown8);
		writer.writeInt(unknown9);
	}
	
	public void setPayload(MultiuserPDU payload) {
		this.payload = payload;
	}
	
	public void setSourcePort(int sourcePort) {
		this.sourcePort = sourcePort;
	}
	
	public void setDestinationPort(int destinationPort) {
		this.destinationPort = destinationPort;
	}
	
	public void setUnknown2(int unknown2) {
		this.unknown1 = unknown2;
	}
	
	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	
	public void setAcknowledgmentNumber(long acknowledgmentNumber) {
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
		
		if (payloadName.equals("VariableSizePdu")) {
			reader.readInt();
		} else if (payloadName.equals("PduGroup")) {
			int size = reader.readInt();
			MultiuserPDU[] payloads = new MultiuserPDU[size];
			
			for (int i = 0; i < size; i++) {
				payloadName = reader.readString();
				payloads[i] = PAYLOAD_ASSOCIATOR
						.getPayloadInstance(payloadName);
				payloads[i].parse(reader);
			}
			
			payload = payloads[0];
		} else if (!payloadName.isEmpty()) {
			payload = PAYLOAD_ASSOCIATOR.getPayloadInstance(payloadName);
			payload.parse(reader);
		}
		
		sourcePort = reader.readShort() & 0xffff;
		destinationPort = reader.readShort() & 0xffff;
		unknown1 = reader.readInt();
		sequenceNumber = reader.readInt() & 0xffffffffl;
		acknowledgmentNumber = reader.readInt() & 0xffffffffl;
		unknown3 = reader.readByte();
		unknown4 = reader.readByte();
		flags = reader.readByte();
		unknown5 = reader.readShort();
		unknown6 = reader.readShort();
		unknown7 = reader.readShort();
		
		if (unknown7 == 1) {
			reader.readString();
			reader.readInt();
			reader.readInt();
			reader.readInt();
		}
		
		unknown8 = reader.readInt();
		unknown9 = reader.readInt();
	}
	
}