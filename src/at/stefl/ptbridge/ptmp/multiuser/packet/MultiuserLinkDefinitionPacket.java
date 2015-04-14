package at.stefl.ptbridge.ptmp.multiuser.packet;

import java.util.HashMap;
import java.util.Map;

import at.stefl.ptbridge.ptmp.PTMPDataReader;
import at.stefl.ptbridge.ptmp.PTMPDataWriter;
import at.stefl.ptbridge.ptmp.PTMPEncoding;
import at.stefl.ptbridge.ptmp.multiuser.MultiuserInterfaceType;
import at.stefl.ptbridge.ptmp.multiuser.MultiuserLinkDefinition;
import at.stefl.ptbridge.ptmp.multiuser.MultiuserLinkType;
import at.stefl.ptbridge.ptmp.packet.PTMPEncodedPacket;


public class MultiuserLinkDefinitionPacket extends MultiuserPacket {
	
	private static enum LinkTypeTranslator {
		NONE(0, MultiuserLinkType.NONE),
		STRAIGHT_THROUGH(1, MultiuserLinkType.STRAIGHT_THROUGH),
		CROSS_OVER(2, MultiuserLinkType.CROSS_OVER),
		CONSOLE(3, MultiuserLinkType.CONSOLE),
		FIBER(4, MultiuserLinkType.FIBER),
		SERIAL(5, MultiuserLinkType.SERIAL),
		PHONE(6, MultiuserLinkType.PHONE),
		COAXIAL(7, MultiuserLinkType.COAXIAL);
		
		private static final Map<Integer, MultiuserLinkType> decodingMap = new HashMap<Integer, MultiuserLinkType>();
		private static final Map<MultiuserLinkType, Integer> encodingMap = new HashMap<MultiuserLinkType, Integer>();
		
		static {
			for (LinkTypeTranslator translator : values()) {
				decodingMap.put(translator.rawLinkType, translator.linkType);
				encodingMap.put(translator.linkType, translator.rawLinkType);
			}
		}
		
		public static MultiuserLinkType decode(int rawLinkType) {
			MultiuserLinkType result = decodingMap.get(rawLinkType);
			if (result == null)
				throw new IllegalStateException("Unsupported link type!");
			return result;
		}
		
		public static int encode(MultiuserLinkType linkType) {
			Integer result = encodingMap.get(linkType);
			if (result == null)
				throw new IllegalStateException("Unsupported link type!");
			return result;
		}
		
		private final int rawLinkType;
		private final MultiuserLinkType linkType;
		
		private LinkTypeTranslator(int rawLinkType, MultiuserLinkType linkType) {
			this.rawLinkType = rawLinkType;
			this.linkType = linkType;
		}
	}
	
	private static enum InterfaceTypeTranslator {
		CONSOLE(0, MultiuserInterfaceType.CONSOLE),
		COPPER_ETHERNET(2, MultiuserInterfaceType.COPPER_ETHERNET),
		COPPER_FAST_ETHERNET(3, MultiuserInterfaceType.COPPER_FAST_ETHERNET),
		COPPER_GIGABIT_ETHERNET(4,
				MultiuserInterfaceType.COPPER_GIGABIT_ETHERNET),
		FIBER_FAST_ETHERNET(5, MultiuserInterfaceType.FIBER_FAST_ETHERNET),
		FIBER_GIGABIT_ETHERNET(6,
				MultiuserInterfaceType.COPPER_GIGABIT_ETHERNET),
		SERIAL_BIG(7, MultiuserInterfaceType.SERIAL_BIG),
		SERIAL(8, MultiuserInterfaceType.SERIAL),
		ANALOG_PHONE(18, MultiuserInterfaceType.ANALOG_PHONE),
		COAXIAL(21, MultiuserInterfaceType.COAXIAL);
		
		private static final Map<Integer, MultiuserInterfaceType> decodingMap = new HashMap<Integer, MultiuserInterfaceType>();
		private static final Map<MultiuserInterfaceType, Integer> encodingMap = new HashMap<MultiuserInterfaceType, Integer>();
		
		static {
			for (InterfaceTypeTranslator translator : values()) {
				decodingMap.put(translator.rawInterfaceType,
						translator.interfaceType);
				encodingMap.put(translator.interfaceType,
						translator.rawInterfaceType);
			}
		}
		
		public static MultiuserInterfaceType decode(int rawInterfaceType) {
			MultiuserInterfaceType result = decodingMap.get(rawInterfaceType);
			if (result == null)
				throw new IllegalStateException("Unsupported interface type!");
			return result;
		}
		
		public static int encode(MultiuserInterfaceType interfaceType) {
			Integer result = encodingMap.get(interfaceType);
			if (result == null)
				throw new IllegalStateException("Unsupported interface type!");
			return result;
		}
		
		private final int rawInterfaceType;
		private final MultiuserInterfaceType interfaceType;
		
		private InterfaceTypeTranslator(int rawInterfaceType,
				MultiuserInterfaceType interfaceType) {
			this.rawInterfaceType = rawInterfaceType;
			this.interfaceType = interfaceType;
		}
	}
	
	public static enum ChangeType {
		NEW(0),
		OLD(1),
		DETACH(2),
		REMOVE(3);
		
		private static final Map<Integer, ChangeType> decodingMap = new HashMap<Integer, ChangeType>();
		
		static {
			for (ChangeType translator : values()) {
				decodingMap.put(translator.rawChangeType, translator);
			}
		}
		
		public static ChangeType decode(int rawChangeType) {
			ChangeType result = decodingMap.get(rawChangeType);
			if (result == null)
				throw new IllegalStateException("Unsupported encoding!");
			return result;
		}
		
		public static int encode(ChangeType changeType) {
			return changeType.rawChangeType;
		}
		
		private int rawChangeType;
		
		private ChangeType(int rawChangeType) {
			this.rawChangeType = rawChangeType;
		}
	}
	
	public static final int TYPE = TYPE_LINK_DEFINITION;
	
	private ChangeType changeType;
	private int linkId;
	
	private MultiuserLinkDefinition definition;
	
	public MultiuserLinkDefinitionPacket(ChangeType changeType, int linkId,
			MultiuserLinkDefinition definition) {
		super(TYPE);
		
		this.changeType = changeType;
		this.linkId = linkId;
		this.definition = definition;
	}
	
	public MultiuserLinkDefinitionPacket(PTMPDataReader reader) {
		super(reader);
	}
	
	public MultiuserLinkDefinitionPacket(byte[] packet, PTMPEncoding encoding) {
		super(packet, encoding);
	}
	
	public MultiuserLinkDefinitionPacket(PTMPEncodedPacket packet) {
		super(packet);
	}
	
	public MultiuserLinkDefinitionPacket(MultiuserLinkDefinitionPacket packet) {
		super(packet);
		
		this.changeType = packet.changeType;
		this.linkId = packet.linkId;
		this.definition = packet.definition;
	}
	
	public ChangeType getChangeType() {
		return changeType;
	}
	
	public int getLinkId() {
		return linkId;
	}
	
	public MultiuserLinkDefinition getDefinition() {
		return definition;
	}
	
	@Override
	public void getValue(PTMPDataWriter writer) {
		writer.writeInt(0);
		writer.writeInt(ChangeType.encode(changeType));
		writer.writeInt(linkId);
		writer.writeUuid(definition.getUuid());
		writer.writeInt(-1);
		writer.writeInt(4); // TODO: WHAT?!
		writer.writeInt(LinkTypeTranslator.encode(definition.getType()));
		writer.writeString(definition.getInterfaceName());
		writer.writeInt(InterfaceTypeTranslator.encode(definition
				.getInterfaceType()));
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
	
	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
	}
	
	public void setLinkId(int linkId) {
		this.linkId = linkId;
	}
	
	public void setDefinition(MultiuserLinkDefinition definition) {
		this.definition = definition;
	}
	
	@Override
	public void parseValue(PTMPDataReader reader) {
		reader.readInt();
		changeType = ChangeType.decode(reader.readInt());
		linkId = reader.readInt();
		definition = new MultiuserLinkDefinition(reader.readUuid());
		reader.readInt();
		reader.readInt(); // TODO: WHAT?!
		definition.setType(LinkTypeTranslator.decode(reader.readInt()));
		definition.setInterfaceName(reader.readString());
		definition.setInterfaceType(InterfaceTypeTranslator.decode(reader
				.readInt()));
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
	
	@Override
	protected boolean legalType(int type) {
		return type == TYPE;
	}
	
}