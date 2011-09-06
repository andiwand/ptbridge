package at.andiwand.packettracer.ptmp.packet;

import at.andiwand.packettracer.ptmp.PTMPDataReader;
import at.andiwand.packettracer.ptmp.PTMPDataWriter;


public class PTMPAuthenticationRequestPacket extends PTMPPacket {
	
	public static final int TYPE = TYPE_AUTHENTICATION_REQUEST;
	
	
	
	
	private String username;
	
	
	
	public PTMPAuthenticationRequestPacket(String username) {
		super(TYPE);
		
		this.username = username;
	}
	public PTMPAuthenticationRequestPacket(PTMPDataReader reader) {
		super(reader);
	}
	public PTMPAuthenticationRequestPacket(byte[] packet, int encoding) {
		super(packet, encoding);
	}
	public PTMPAuthenticationRequestPacket(PTMPEncodedPacket packet) {
		super(packet);
	}
	public PTMPAuthenticationRequestPacket(
			PTMPAuthenticationRequestPacket packet) {
		super(packet);
		
		username = packet.username;
	}
	
	
	
	public String getUsername() {
		return username;
	}
	public void getValue(PTMPDataWriter writer) {
		writer.writeString(username);
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	public void parseValue(PTMPDataReader reader) {
		username = reader.readString();
	}
	
	
	protected boolean legalType(int type) {
		return type == TYPE;
	}
	
}