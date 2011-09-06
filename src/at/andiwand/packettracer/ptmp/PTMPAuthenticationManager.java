package at.andiwand.packettracer.ptmp;


public interface PTMPAuthenticationManager {
	
	public boolean chellengeUser(String username, String digest,
			PTMPAuthenticationMethod authenticationMethod);
	
}