package at.andiwand.packettracer.ptmp.multiuser.pdu;

import at.andiwand.packettracer.ptmp.PTMPDataReader;
import at.andiwand.packettracer.ptmp.PTMPDataWriter;


public class MultiuserUDPSegment extends MultiuserProtocolDataUnit {
	
	private static final MultiuserPayloadAssociator PAYLOAD_ASSOCIATOR = new MultiuserPayloadAssociator();
	
	static {
		PAYLOAD_ASSOCIATOR.putEntry("CDhcpPacket", MultiuserDHCPPacket.class);
	}
	
	
	
	
	private MultiuserProtocolDataUnit payload;
	private short sourcePort;
	private short destinationPort;
	
	
	
	public MultiuserProtocolDataUnit getPayload() {
		return payload;
	}
	public short getSourcePort() {
		return sourcePort;
	}
	public short getDestinationPort() {
		return destinationPort;
	}
	public void getBytes(PTMPDataWriter writer) {
		Class<? extends MultiuserProtocolDataUnit> payloadClass = payload.getClass();
		String payloadName = PAYLOAD_ASSOCIATOR.getPayloadName(payloadClass);
		writer.writeString(payloadName);
		payload.getBytes(writer);
		
		writer.writeShort(sourcePort);
		writer.writeShort(destinationPort);
		writer.writeShort(0); // length
		writer.writeShort(0); // checksum
	}
	
	public void setPayload(MultiuserProtocolDataUnit payload) {
		this.payload = payload;
	}
	public void setSourcePort(short sourcePort) {
		this.sourcePort = sourcePort;
	}
	public void setDestinationPort(short destinationPort) {
		this.destinationPort = destinationPort;
	}
	
	
	public void parse(PTMPDataReader reader) {
		String payloadName = reader.readString();
		payload = PAYLOAD_ASSOCIATOR.getPayloadInstance(payloadName);
		payload.parse(reader);
		
		sourcePort = reader.readShort();
		destinationPort = reader.readShort();
		reader.readShort(); // length
		reader.readShort(); // checksum
	}
	
}