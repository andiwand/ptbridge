package at.stefl.ptbridge.ptmp.multiuser.pdu;

import at.stefl.ptbridge.ptmp.PTMPDataReader;


public class MultiuserVariableSizePDUKiller {
	
	public static final String HEAD = "VariableSizePdu";
	
	public static void kill(PTMPDataReader dataReader) {
		String variableSizePdu = dataReader.readString();
		
		if (variableSizePdu.isEmpty()) return;
		
		if (!variableSizePdu.equals(HEAD))
			throw new IllegalStateException("No variable size PDU");
		
		dataReader.readInt();
//		int size = dataReader.readInt();
//		if (dataReader.readInt() != size)
//			throw new IllegalStateException("Different size");
		
//		for (int i = 0; i < size; i++) {
//			dataReader.readString();
//		}
	}
	
}