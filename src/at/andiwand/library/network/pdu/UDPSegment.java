package at.andiwand.library.network.pdu;


public class UDPSegment extends ProtocolDataUnit {
	
	private int sourcePort;
	private int destinationPort;
	private ProtocolDataUnit payload;
	
	
	public int getSourcePort() {
		return sourcePort;
	}
	public int getDestinationPort() {
		return destinationPort;
	}
	public ProtocolDataUnit getPayload() {
		return payload;
	}
	
	public void setSourcePort(int sourcePort) {
		this.sourcePort = sourcePort;
	}
	public void setDestinationPort(int destinationPort) {
		this.destinationPort = destinationPort;
	}
	public void setPayload(ProtocolDataUnit payload) {
		this.payload = payload;
	}
	
}