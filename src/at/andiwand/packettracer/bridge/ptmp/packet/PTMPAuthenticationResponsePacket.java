package at.andiwand.packettracer.bridge.ptmp.packet;

import at.andiwand.packettracer.bridge.ptmp.PTMPDataReader;
import at.andiwand.packettracer.bridge.ptmp.PTMPDataWriter;
import at.andiwand.packettracer.bridge.ptmp.PTMPEncoding;


public class PTMPAuthenticationResponsePacket extends PTMPPacket {
	
	public static final int TYPE = TYPE_AUTHENTICATION_RESPONSE;
	
	private String username;
	private String digestText;
	
	public PTMPAuthenticationResponsePacket(String username, String digestText) {
		super(TYPE);
		
		this.username = username;
		this.digestText = digestText;
	}
	
	public PTMPAuthenticationResponsePacket(PTMPDataReader reader) {
		super(reader);
	}
	
	public PTMPAuthenticationResponsePacket(byte[] packet, PTMPEncoding encoding) {
		super(packet, encoding);
	}
	
	public PTMPAuthenticationResponsePacket(PTMPEncodedPacket packet) {
		super(packet);
	}
	
	public PTMPAuthenticationResponsePacket(
			PTMPAuthenticationResponsePacket packet) {
		super(packet);
		
		username = packet.username;
		digestText = packet.digestText;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getDigestText() {
		return digestText;
	}
	
	public void getValue(PTMPDataWriter writer) {
		writer.writeString(username);
		writer.writeString(digestText);
		writer.writeString("");
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setDigestText(String digestText) {
		this.digestText = digestText;
	}
	
	public void parseValue(PTMPDataReader reader) {
		username = reader.readString();
		digestText = reader.readString();
		reader.readString();
	}
	
	protected boolean legalType(int type) {
		return type == TYPE;
	}
	
}