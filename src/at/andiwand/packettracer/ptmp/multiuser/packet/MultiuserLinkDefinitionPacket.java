package at.andiwand.packettracer.ptmp.multiuser.packet;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import at.andiwand.packettracer.ptmp.PTMPDataReader;
import at.andiwand.packettracer.ptmp.PTMPDataWriter;
import at.andiwand.packettracer.ptmp.multiuser.MultiuserLinkDefinition;
import at.andiwand.packettracer.ptmp.packet.PTMPEncodedPacket;


public class MultiuserLinkDefinitionPacket extends MultiuserPacket {
	
	public static final int TYPE = TYPE_LINK_DEFINITION;
	
	public static final int CHANGE_TYPE_NEW		= 0;
	public static final int CHANGE_TYPE_OLD		= 1;
	public static final int CHANGE_TYPE_DETACH	= 2;
	public static final int CHANGE_TYPE_REMOVE	= 3;
	public static final Set<Integer> CHANGE_TYPE_SET = Collections
			.unmodifiableSet(new HashSet<Integer>(Arrays.asList(
					CHANGE_TYPE_NEW, CHANGE_TYPE_OLD, CHANGE_TYPE_DETACH,
					CHANGE_TYPE_REMOVE)));
	
	
	public static boolean legalChangeType(int changeType) {
		return CHANGE_TYPE_SET.contains(changeType);
	}
	
	
	
	
	private int changeType;
	private int linkId;
	
	private MultiuserLinkDefinition definition;
	
	
	
	public MultiuserLinkDefinitionPacket(int changeType, int linkId, MultiuserLinkDefinition definition) {
		super(TYPE);
		
		setChangeType(changeType);
		this.linkId = linkId;
		this.definition = definition;
	}
	public MultiuserLinkDefinitionPacket(PTMPDataReader reader) {
		super(reader);
	}
	public MultiuserLinkDefinitionPacket(byte[] packet, int encoding) {
		super(packet, encoding);
	}
	public MultiuserLinkDefinitionPacket(PTMPEncodedPacket packet) {
		super(packet);
	}
	public MultiuserLinkDefinitionPacket(MultiuserLinkDefinitionPacket packet) {
		super(packet);
		
		changeType = packet.changeType;
		linkId = packet.linkId;
		definition = packet.definition;
	}
	
	
	
	public int getChangeType() {
		return changeType;
	}
	public int getLinkId() {
		return linkId;
	}
	public MultiuserLinkDefinition getDefinition() {
		return definition;
	}
	public void getValue(PTMPDataWriter writer) {
		writer.writeInt(0);
		writer.writeInt(changeType);
		writer.writeInt(linkId);
		writer.writeUuid(definition.getUuid());
		writer.writeInt(-1);
		writer.writeInt(definition.getType());
		writer.writeString(definition.getInterfaceName());
		writer.writeInt(definition.getInterfaceType());
		writer.writeBoolean(definition.isInterfaceUp());
		writer.writeBoolean(definition.isInterfaceCrossing());
		writer.writeBoolean(false);
		writer.writeInt(definition.getBandwidth());
		writer.writeBoolean(definition.isFullDuplex());
		writer.writeBoolean(true);
		writer.writeBoolean(definition.isAutoBandwidth());
		writer.writeBoolean(definition.isAutoDuplex());
		writer.writeInt(definition.getClockRate());
		writer.writeBoolean(definition.isDCE());
		writer.writeInt(1);
		writer.writeInt(1);
		writer.writeBoolean(false);
		writer.writeBoolean(false);
		writer.writeBoolean(definition.isDeviceUp());
	}
	
	public void setChangeType(int changeType) {
		if (!legalChangeType(changeType))
			throw new IllegalArgumentException("Illegal change type");
		
		this.changeType = changeType;
	}
	public void setLinkId(int linkId) {
		this.linkId = linkId;
	}
	public void setDefinition(MultiuserLinkDefinition definition) {
		this.definition = definition;
	}
	
	
	public void parseValue(PTMPDataReader reader) {
		reader.readInt();
		changeType = reader.readInt();
		linkId = reader.readInt();
		definition = new MultiuserLinkDefinition(reader.readUuid());
		reader.readInt();
		definition.setType(reader.readInt());
		definition.setInterfaceName(reader.readString());
		definition.setInterfaceType(reader.readInt());
		definition.setInterfaceUp(reader.readBoolean());
		definition.setInterfaceCrossing(reader.readBoolean());
		reader.readBoolean();
		definition.setBandwidth(reader.readInt());
		definition.setFullDuplex(reader.readBoolean());
		reader.readBoolean();
		definition.setAutoBandwidth(reader.readBoolean());
		definition.setAutoDuplex(reader.readBoolean());
		definition.setClockRate(reader.readInt());
		definition.setDCE(reader.readBoolean());
		reader.readInt();
		reader.readInt();
		reader.readBoolean();
		reader.readBoolean();
		definition.setDeviceUp(reader.readBoolean());
	}
	
	
	protected boolean legalType2(int type) {
		return type == TYPE;
	}
	
}