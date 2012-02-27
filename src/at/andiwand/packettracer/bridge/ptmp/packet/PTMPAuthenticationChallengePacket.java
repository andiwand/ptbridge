package at.andiwand.packettracer.bridge.ptmp.packet;

import at.andiwand.packettracer.bridge.ptmp.PTMPDataReader;
import at.andiwand.packettracer.bridge.ptmp.PTMPDataWriter;
import at.andiwand.packettracer.bridge.ptmp.PTMPEncoding;


public class PTMPAuthenticationChallengePacket extends PTMPPacket {
	
	public static final int TYPE = TYPE_AUTHENTICATION_CHALLENGE;
	
	private String challengeText;
	
	public PTMPAuthenticationChallengePacket(String challengeText) {
		super(TYPE);
		
		this.challengeText = challengeText;
	}
	
	public PTMPAuthenticationChallengePacket(PTMPDataReader reader) {
		super(reader);
	}
	
	public PTMPAuthenticationChallengePacket(byte[] packet,
			PTMPEncoding encoding) {
		super(packet, encoding);
	}
	
	public PTMPAuthenticationChallengePacket(PTMPEncodedPacket packet) {
		super(packet);
	}
	
	public PTMPAuthenticationChallengePacket(
			PTMPAuthenticationChallengePacket packet) {
		super(packet);
		
		challengeText = packet.challengeText;
	}
	
	public String getChallengeText() {
		return challengeText;
	}
	
	public void getValue(PTMPDataWriter writer) {
		writer.writeString(challengeText);
	}
	
	public void setChallengeText(String challengeText) {
		this.challengeText = challengeText;
	}
	
	public void parseValue(PTMPDataReader reader) {
		challengeText = reader.readString();
	}
	
	protected boolean legalType(int type) {
		return type == TYPE;
	}
	
}