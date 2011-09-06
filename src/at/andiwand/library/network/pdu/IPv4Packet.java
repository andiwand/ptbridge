package at.andiwand.library.network.pdu;

import java.net.Inet4Address;


//http://www.iana.org/assignments/protocol-numbers/protocol-numbers.xml
public class IPv4Packet extends ProtocolDataUnit {
	
	public static final byte VERSION = 4;
	
	public static final byte PROTOCOL_ICMP = 1;
	public static final byte PROTOCOL_TCP = 6;
	public static final byte PROTOCOL_UDP = 17;
	
	public static final byte FLAGS_DONT_FRAGMENT = 2;
	public static final byte FLAGS_MORE_FRAGMENTS = 4;
	
	
	
	private byte version;
	private byte typeOfService;
	private int identication;
	private byte flags;
	private short fragmentOffset;
	private short timeToLive;
	private byte protocol;
	private Inet4Address source;
	private Inet4Address destination;
	private ProtocolDataUnit payload;
	
	
	public byte getVersion() {
		return version;
	}
	public byte getTypeOfService() {
		return typeOfService;
	}
	public int getIdentication() {
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
	public byte getProtocol() {
		return protocol;
	}
	public Inet4Address getSource() {
		return source;
	}
	public Inet4Address getDestination() {
		return destination;
	}
	public ProtocolDataUnit getPayload() {
		return payload;
	}
	
	public void setVersion(byte version) {
		this.version = version;
	}
	public void setTypeOfService(byte typeOfService) {
		this.typeOfService = typeOfService;
	}
	public void setIdentication(int identication) {
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
	public void setProtocol(byte protocol) {
		this.protocol = protocol;
	}
	public void setSource(Inet4Address source) {
		this.source = source;
	}
	public void setDestination(Inet4Address destination) {
		this.destination = destination;
	}
	public void setPayload(ProtocolDataUnit payload) {
		this.payload = payload;
	}
	
}