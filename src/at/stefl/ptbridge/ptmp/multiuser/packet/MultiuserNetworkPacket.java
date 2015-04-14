package at.stefl.ptbridge.ptmp.multiuser.packet;

import at.stefl.ptbridge.ptmp.PTMPDataReader;
import at.stefl.ptbridge.ptmp.PTMPDataWriter;
import at.stefl.ptbridge.ptmp.PTMPEncoding;
import at.stefl.ptbridge.ptmp.multiuser.pdu.MultiuserEthernet2Frame;
import at.stefl.ptbridge.ptmp.multiuser.pdu.MultiuserPDU;
import at.stefl.ptbridge.ptmp.multiuser.pdu.MultiuserPayloadAssociator;
import at.stefl.ptbridge.ptmp.packet.PTMPEncodedPacket;


public class MultiuserNetworkPacket extends MultiuserPacket {
	
	public static final int TYPE = TYPE_NETWORK_PACKET;
	
	private static final MultiuserPayloadAssociator PAYLOAD_ASSOCIATOR = new MultiuserPayloadAssociator();
	
	static {
		PAYLOAD_ASSOCIATOR.putEntry("EthernetIIHeader",
				MultiuserEthernet2Frame.class);
	}
	
	private int random;
	private int linkId;
	private MultiuserPDU payload;
	
	public MultiuserNetworkPacket(int linkId, MultiuserPDU payload) {
		this(0, linkId, payload);
	}
	
	public MultiuserNetworkPacket(int random, int linkId, MultiuserPDU payload) {
		super(TYPE);
		
		this.random = random;
		this.linkId = linkId;
		this.payload = payload;
	}
	
	public MultiuserNetworkPacket(PTMPDataReader reader) {
		super(reader);
	}
	
	public MultiuserNetworkPacket(byte[] packet, PTMPEncoding encoding) {
		super(packet, encoding);
	}
	
	public MultiuserNetworkPacket(PTMPEncodedPacket packet) {
		super(packet);
	}
	
	public MultiuserNetworkPacket(MultiuserNetworkPacket packet) {
		super(packet);
		
		random = packet.random;
		linkId = packet.linkId;
		payload = packet.payload;
	}
	
	public int getRandom() {
		return random;
	}
	
	public int getLinkId() {
		return linkId;
	}
	
	public MultiuserPDU getPayload() {
		return payload;
	}
	
	@Override
	public void getValue(PTMPDataWriter writer) {
		writer.writeInt(random);
		writer.writeInt(linkId);
		
		Class<? extends MultiuserPDU> payloadClass = payload.getClass();
		String payloadName = PAYLOAD_ASSOCIATOR.getPayloadName(payloadClass);
		writer.writeString(payloadName);
		payload.getBytes(writer);
	}
	
	public void setRandom(int random) {
		this.random = random;
	}
	
	public void setLinkId(int linkId) {
		this.linkId = linkId;
	}
	
	public void setPayload(MultiuserPDU payload) {
		this.payload = payload;
	}
	
	@Override
	public void parseValue(PTMPDataReader reader) {
		random = reader.readInt();
		linkId = reader.readInt();
		
		String payloadName = reader.readString();
		payload = PAYLOAD_ASSOCIATOR.getPayloadInstance(payloadName);
		if (payload != null) payload.parse(reader);
	}
	
	@Override
	protected boolean legalType(int type) {
		return type == TYPE;
	}
	
}