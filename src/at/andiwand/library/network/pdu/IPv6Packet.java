package at.andiwand.library.network.pdu;

import java.net.Inet6Address;


public class IPv6Packet extends ProtocolDataUnit {
	
	public static final byte VERSION = 4;
	
	
	
	private byte version;
	private byte trafficClass;
	private int flowLabel;
	private byte nextHeader;
	private short hopLimit;
	private Inet6Address source;
	private Inet6Address destination;
	private ProtocolDataUnit payload;
	
	
	public byte getVersion() {
		return version;
	}
	public byte getTrafficClass() {
		return trafficClass;
	}
	public int getFlowLabel() {
		return flowLabel;
	}
	public byte getNextHeader() {
		return nextHeader;
	}
	public short getHopLimit() {
		return hopLimit;
	}
	public Inet6Address getSource() {
		return source;
	}
	public Inet6Address getDestination() {
		return destination;
	}
	public ProtocolDataUnit getPayload() {
		return payload;
	}
	
	public void setVersion(byte version) {
		this.version = version;
	}
	public void setTrafficClass(byte trafficClass) {
		this.trafficClass = trafficClass;
	}
	public void setFlowLabel(int flowLabel) {
		this.flowLabel = flowLabel;
	}
	public void setNextHeader(byte nextHeader) {
		this.nextHeader = nextHeader;
	}
	public void setHopLimit(short hopLimit) {
		this.hopLimit = hopLimit;
	}
	public void setSource(Inet6Address source) {
		this.source = source;
	}
	public void setDestination(Inet6Address destination) {
		this.destination = destination;
	}
	public void setPayload(ProtocolDataUnit payload) {
		this.payload = payload;
	}
	
}