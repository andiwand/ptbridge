package at.stefl.ptbridge.ptmp.multiuser.pdu;

import at.stefl.commons.network.mac.MACAddress;
import at.stefl.ptbridge.ptmp.PTMPDataReader;
import at.stefl.ptbridge.ptmp.PTMPDataWriter;


// TODO: fix attribute types
public class MultiuserEthernet2Frame extends MultiuserPDU {
	
	private static final MultiuserPayloadAssociator PAYLOAD_ASSOCIATOR = new MultiuserPayloadAssociator();
	
	static {
		PAYLOAD_ASSOCIATOR.putEntry("IpHeader", MultiuserIPv4Packet.class);
		PAYLOAD_ASSOCIATOR.putEntry("ArpPacket", MultiuserARPPacket.class);
		// PAYLOAD_ASSOCIATOR.putEntry("CIpv6Header",
		// MultiuserIPv6Packet.class);
	}
	
	private MultiuserPDU payload;
	private MACAddress source;
	private MACAddress destination;
	private short unknown1;
	private int type;
	
	public MultiuserPDU getPayload() {
		return payload;
	}
	
	public MACAddress getSource() {
		return source;
	}
	
	public MACAddress getDestination() {
		return destination;
	}
	
	public short getUnknown1() {
		return unknown1;
	}
	
	public int getType() {
		return type;
	}
	
	public void getBytes(PTMPDataWriter writer) {
		Class<? extends MultiuserPDU> payloadClass = payload.getClass();
		String payloadName = PAYLOAD_ASSOCIATOR.getPayloadName(payloadClass);
		writer.writeString(payloadName);
		payload.getBytes(writer);
		
		writer.writeMACAddress(destination);
		writer.writeMACAddress(source);
		writer.writeShort(unknown1);
		writer.writeInt(type);
	}
	
	public void setPayload(MultiuserPDU payload) {
		this.payload = payload;
	}
	
	public void setSource(MACAddress source) {
		this.source = source;
	}
	
	public void setDestination(MACAddress destination) {
		this.destination = destination;
	}
	
	public void setUnknown1(short unknown1) {
		this.unknown1 = unknown1;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public void parse(PTMPDataReader reader) {
		String payloadName = reader.readString();
		payload = PAYLOAD_ASSOCIATOR.getPayloadInstance(payloadName);
		payload.parse(reader);
		
		destination = reader.readMACAddress();
		source = reader.readMACAddress();
		unknown1 = reader.readShort();
		type = reader.readInt();
	}
	
}