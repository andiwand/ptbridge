package at.andiwand.library.network.pdu;

import at.andiwand.library.network.mac.MACAddress;


//http://www.iana.org/assignments/ethernet-numbers
public class Ethernet2Frame extends ProtocolDataUnit {
	
	private MACAddress destination;
	private MACAddress source;
	private short type;
	private ProtocolDataUnit payload;
	
	
	public MACAddress getDestination() {
		return destination;
	}
	public MACAddress getSource() {
		return source;
	}
	public short getType() {
		return type;
	}
	public ProtocolDataUnit getPayload() {
		return payload;
	}
	
	public void setDestination(MACAddress destination) {
		this.destination = destination;
	}
	public void setSource(MACAddress source) {
		this.source = source;
	}
	public void setType(short type) {
		this.type = type;
	}
	public void setPayload(ProtocolDataUnit payload) {
		this.payload = payload;
	}
	
}