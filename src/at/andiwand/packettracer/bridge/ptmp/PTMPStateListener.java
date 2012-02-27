package at.andiwand.packettracer.bridge.ptmp;

public interface PTMPStateListener {
	
	public void stateChanged(PTMPState state, PTMPState oldState);
	
}