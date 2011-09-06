package at.andiwand.packettracer.ptmp.multiuser.packet;

import at.andiwand.packettracer.ptmp.PTMPDataReader;
import at.andiwand.packettracer.ptmp.PTMPDataWriter;
import at.andiwand.packettracer.ptmp.packet.PTMPEncodedPacket;


public class MultiuserXYZPacket extends MultiuserPacket {
	
	public static final int TYPE = TYPE_XYZ;
	
	
	
	
	private int xyz;
	
	
	
	public MultiuserXYZPacket(int xyz) {
		super(TYPE);
		
		this.xyz = xyz;
	}
	public MultiuserXYZPacket(PTMPDataReader reader) {
		super(reader);
	}
	public MultiuserXYZPacket(byte[] packet, int encoding) {
		super(packet, encoding);
	}
	public MultiuserXYZPacket(PTMPEncodedPacket packet) {
		super(packet);
	}
	public MultiuserXYZPacket(MultiuserXYZPacket packet) {
		super(packet);
		
		xyz = packet.xyz;
	}
	
	
	
	public int getXyz() {
		return xyz;
	}
	public void getValue(PTMPDataWriter writer) {
		writer.writeInt(xyz);
	}
	
	public void setXyz(int xyz) {
		this.xyz = xyz;
	}
	
	
	public void parseValue(PTMPDataReader reader) {
		xyz = reader.readInt();
	}
	
	
	protected boolean legalType2(int type) {
		return type == TYPE;
	}
	
}