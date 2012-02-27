package at.andiwand.packettracer.bridge.ptmp.packet;

import at.andiwand.packettracer.bridge.ptmp.PTMPDataReader;
import at.andiwand.packettracer.bridge.ptmp.PTMPDataWriter;
import at.andiwand.packettracer.bridge.ptmp.PTMPEncoding;


public class PTMPAuthenticationStatusPacket extends PTMPPacket {
	
	public static final int TYPE = TYPE_AUTHENTICATION_STATUS;
	
	private boolean status;
	
	public PTMPAuthenticationStatusPacket(boolean status) {
		super(TYPE);
		
		this.status = status;
	}
	
	public PTMPAuthenticationStatusPacket(PTMPDataReader reader) {
		super(reader);
	}
	
	public PTMPAuthenticationStatusPacket(byte[] packet, PTMPEncoding encoding) {
		super(packet, encoding);
	}
	
	public PTMPAuthenticationStatusPacket(PTMPEncodedPacket packet) {
		super(packet);
	}
	
	public PTMPAuthenticationStatusPacket(PTMPAuthenticationStatusPacket packet) {
		super(packet);
		
		status = packet.status;
	}
	
	public boolean getStatus() {
		return status;
	}
	
	public void getValue(PTMPDataWriter writer) {
		writer.writeBoolean(status);
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public void parseValue(PTMPDataReader reader) {
		status = reader.readBoolean();
	}
	
	protected boolean legalType(int type) {
		return type == TYPE;
	}
	
}