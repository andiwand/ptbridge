package at.stefl.ptbridge.ptmp.multiuser.pdu;

import at.stefl.commons.network.ip.IPv4Address;
import at.stefl.ptbridge.ptmp.PTMPDataReader;
import at.stefl.ptbridge.ptmp.PTMPDataWriter;


// TODO: fix attribute types
public class MultiuserIPv4Packet extends MultiuserPDU {
	
	private static final MultiuserPayloadAssociator PAYLOAD_ASSOCIATOR = new MultiuserPayloadAssociator();
	
	static {
		PAYLOAD_ASSOCIATOR.putEntry("IcmpMessage", MultiuserICMPPacket.class);
		PAYLOAD_ASSOCIATOR.putEntry("TcpHeader", MultiuserTCPSegment.class);
		PAYLOAD_ASSOCIATOR.putEntry("UdpHeader", MultiuserUDPSegment.class);
	}
	
	private MultiuserPDU payload;
	private byte version;
	private byte typeOfService;
	private short identication;
	private byte flags;
	private short fragmentOffset;
	private short timeToLive;
	private short protocol;
	private IPv4Address source;
	private IPv4Address destination;
	private int unknown1;
	private int unknown2;
	
	public MultiuserPDU getPayload() {
		return payload;
	}
	
	public byte getVersion() {
		return version;
	}
	
	public byte getTypeOfService() {
		return typeOfService;
	}
	
	public short getIdentication() {
		return identication;
	}
	
	public byte getFlags() {
		return flags;
	}
	
	public short getFragmentOffset() {
		return fragmentOffset;
	}
	
	public short getTimeToLive() {
		return timeToLive;
	}
	
	public short getProtocol() {
		return protocol;
	}
	
	public IPv4Address getSource() {
		return source;
	}
	
	public IPv4Address getDestination() {
		return destination;
	}
	
	public int getUnknown1() {
		return unknown1;
	}
	
	public int getUnknown2() {
		return unknown2;
	}
	
	public void getBytes(PTMPDataWriter writer) {
		Class<? extends MultiuserPDU> payloadClass = payload.getClass();
		String payloadName = PAYLOAD_ASSOCIATOR.getPayloadName(payloadClass);
		writer.writeString(payloadName);
		payload.getBytes(writer);
		
		writer.writeByte(version);
		writer.writeByte(5); // IHL
		writer.writeByte(typeOfService);
		writer.writeShort(128); // total length
		writer.writeShort(identication);
		writer.writeByte(flags);
		writer.writeShort(fragmentOffset);
		writer.writeShort(timeToLive);
		writer.writeShort(protocol);
		writer.writeShort(0); // checksum
		writer.writeIPv4Address(source);
		writer.writeIPv4Address(destination);
		writer.writeInt(unknown1);
		writer.writeInt(unknown2);
	}
	
	public void setPayload(MultiuserPDU payload) {
		this.payload = payload;
	}
	
	public void setVersion(byte version) {
		this.version = version;
	}
	
	public void setTypeOfService(byte typeOfService) {
		this.typeOfService = typeOfService;
	}
	
	public void setIdentication(short identication) {
		this.identication = identication;
	}
	
	public void setFlags(byte flags) {
		this.flags = flags;
	}
	
	public void setFragmentOffset(short fragmentOffset) {
		this.fragmentOffset = fragmentOffset;
	}
	
	public void setTimeToLive(short timeToLive) {
		this.timeToLive = timeToLive;
	}
	
	public void setProtocol(short protocol) {
		this.protocol = protocol;
	}
	
	public void setSource(IPv4Address source) {
		this.source = source;
	}
	
	public void setDestination(IPv4Address destination) {
		this.destination = destination;
	}
	
	public void setUnknown1(int unknown1) {
		this.unknown1 = unknown1;
	}
	
	public void setUnknown2(int unknown2) {
		this.unknown2 = unknown2;
	}
	
	public void parse(PTMPDataReader reader) {
		String payloadName = reader.readString();
		payload = PAYLOAD_ASSOCIATOR.getPayloadInstance(payloadName);
		payload.parse(reader);
		
		version = reader.readByte();
		reader.readByte(); // IHL
		typeOfService = reader.readByte();
		reader.readShort(); // total length
		identication = reader.readShort();
		flags = reader.readByte();
		fragmentOffset = reader.readShort();
		timeToLive = reader.readShort();
		protocol = reader.readShort();
		reader.readShort(); // checksum
		source = reader.readIPv4Address();
		destination = reader.readIPv4Address();
		unknown1 = reader.readInt();
		unknown2 = reader.readInt();
	}
	
}