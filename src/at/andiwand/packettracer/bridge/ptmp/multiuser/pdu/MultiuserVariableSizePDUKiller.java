package at.andiwand.packettracer.bridge.ptmp.multiuser.pdu;

import at.andiwand.packettracer.bridge.ptmp.PTMPDataReader;


public class MultiuserVariableSizePDUKiller {
	
	public static final String HEAD = "CVariableSizePdu";
	
	public static void kill(PTMPDataReader dataReader) {
		String variableSizePdu = dataReader.readString();
		
		if (variableSizePdu.isEmpty()) return;
		
		if (!variableSizePdu.equals(HEAD))
			throw new IllegalStateException("No variable size PDU");
		
		int size = dataReader.readInt();
		if (dataReader.readInt() != size)
			throw new IllegalStateException("Different size");
		
		for (int i = 0; i < size; i++) {
			dataReader.readString();
		}
	}
	
}