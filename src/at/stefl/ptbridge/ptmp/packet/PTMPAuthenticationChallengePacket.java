package at.stefl.ptbridge.ptmp.packet;

import at.stefl.ptbridge.ptmp.PTMPDataReader;
import at.stefl.ptbridge.ptmp.PTMPDataWriter;
import at.stefl.ptbridge.ptmp.PTMPEncoding;


public class PTMPAuthenticationChallengePacket extends PTMPPacket {
	
	public static final int TYPE = TYPE_AUTHENTICATION_CHALLENGE;
	
	private String challengeText;
	
	public PTMPAuthenticationChallengePacket(String challengeText) {
		super(TYPE);
		
		this.challengeText = challengeText;
	}
	
	public PTMPAuthenticationChallengePacket(PTMPDataReader in) {
		super(in);
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
	
	public void getValue(PTMPDataWriter out) {
		out.writeString(challengeText);
	}
	
	public void setChallengeText(String challengeText) {
		this.challengeText = challengeText;
	}
	
	public void parseValue(PTMPDataReader in) {
		challengeText = in.readString();
	}
	
	protected boolean legalType(int type) {
		return type == TYPE;
	}
	
}