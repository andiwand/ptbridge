package at.andiwand.packettracer.ptmp;


public interface PTMPStateListener {
	
	public void stateChanged(PTMPState newState, PTMPState oldState);
	
}