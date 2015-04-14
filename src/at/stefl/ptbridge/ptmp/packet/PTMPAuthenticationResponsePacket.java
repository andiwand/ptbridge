package at.stefl.ptbridge.ptmp.packet;

import at.stefl.ptbridge.ptmp.PTMPDataReader;
import at.stefl.ptbridge.ptmp.PTMPDataWriter;
import at.stefl.ptbridge.ptmp.PTMPEncoding;


public class PTMPAuthenticationResponsePacket extends PTMPPacket {
	
	public static final int TYPE = TYPE_AUTHENTICATION_RESPONSE;
	
	private String username;
	private String digestText;
	
	public PTMPAuthenticationResponsePacket(String username, String digestText) {
		super(TYPE);
		
		this.username = username;
		this.digestText = digestText;
	}
	
	public PTMPAuthenticationResponsePacket(PTMPDataReader in) {
		super(in);
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
	
	public void getValue(PTMPDataWriter out) {
		out.writeString(username);
		out.writeString(digestText);
		out.writeString("");
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setDigestText(String digestText) {
		this.digestText = digestText;
	}
	
	public void parseValue(PTMPDataReader in) {
		username = in.readString();
		digestText = in.readString();
		in.readString();
	}
	
	protected boolean legalType(int type) {
		return type == TYPE;
	}
	
}