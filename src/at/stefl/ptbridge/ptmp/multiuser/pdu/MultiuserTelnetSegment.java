package at.stefl.ptbridge.ptmp.multiuser.pdu;

import at.stefl.ptbridge.ptmp.PTMPDataReader;
import at.stefl.ptbridge.ptmp.PTMPDataWriter;


public class MultiuserTelnetSegment extends MultiuserPDU {
	
	private String message;
	private boolean unknown1;
	private int unknown2;
	
	public String getMessage() {
		return message;
	}
	
	public boolean isUnknown1() {
		return unknown1;
	}
	
	public int getUnknown2() {
		return unknown2;
	}
	
	@Override
	public void getBytes(PTMPDataWriter writer) {
		writer.writeString(message);
		writer.writeBoolean(unknown1);
		writer.writeInt(unknown2);
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setUnknown1(boolean unknown1) {
		this.unknown1 = unknown1;
	}
	
	public void setUnknown2(int unknown2) {
		this.unknown2 = unknown2;
	}
	
	@Override
	public void parse(PTMPDataReader reader) {
		message = reader.readString();
		unknown1 = reader.readBoolean();
		unknown2 = reader.readInt();
	}
	
}