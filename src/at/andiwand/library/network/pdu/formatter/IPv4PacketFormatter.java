package at.andiwand.library.network.pdu.formatter;

import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.network.InternetChecksum;
import at.andiwand.library.network.pdu.Ethernet2Frame;
import at.andiwand.library.network.pdu.IPv4Packet;
import at.andiwand.library.network.pdu.ProtocolDataUnit;
import at.andiwand.library.util.DataReader;
import at.andiwand.library.util.DataWriter;


//TODO fragment offset, options
public class IPv4PacketFormatter extends ProtocolDataUnitFormatter {
	
	private static final Map<Byte, ProtocolDataUnitFormatter> DEFAULT_PAYLOAD_FORMATTERS = new HashMap<Byte, ProtocolDataUnitFormatter>();
	
	static {
		DEFAULT_PAYLOAD_FORMATTERS.put(IPv4Packet.PROTOCOL_ICMP,
				new ICMPPacketFormatter());
//		DEFAULT_PAYLOAD_FORMATTERS.put(IPv4Packet.PROTOCOL_TCP,
//				new TCPSegmentFormatter());
		DEFAULT_PAYLOAD_FORMATTERS.put(IPv4Packet.PROTOCOL_UDP,
				new UDPSegmentFormatter());
	}
	
	
	
	private final Map<Byte, ProtocolDataUnitFormatter> payloadFormatters;
	
	
	public IPv4PacketFormatter() {
		this(true);
	}
	public IPv4PacketFormatter(boolean defaultPayloadFormatters) {
		if (defaultPayloadFormatters) {
			payloadFormatters = new HashMap<Byte, ProtocolDataUnitFormatter>(
					DEFAULT_PAYLOAD_FORMATTERS);
		} else {
			payloadFormatters = new HashMap<Byte, ProtocolDataUnitFormatter>();
		}
	}
	
	
	private ProtocolDataUnitFormatter getPayloadFormatter(byte protocol) {
		ProtocolDataUnitFormatter result = payloadFormatters.get(protocol);
		
		if (result == null)
			throw new IllegalArgumentException("Unregistered protocol");
		
		return result;
	}
	
	public void format(ProtocolDataUnit protocolDataUnit, DataWriter dataWriter) {
		if (!(protocolDataUnit instanceof Ethernet2Frame))
			throw new IllegalArgumentException("Illegal PDU class");
		IPv4Packet packet = (IPv4Packet) protocolDataUnit;
		
		DataWriter packetDataWriter = new DataWriter();
		
		int versionIHL = (packet.getVersion() & 0x0f) << 4;
		packetDataWriter.writeByte(versionIHL);
		packetDataWriter.writeByte(packet.getTypeOfService());
		packetDataWriter.writeShort(0); // total length
		packetDataWriter.writeShort(packet.getIdentication());
		int flagsFragmentOffset = ((packet.getFlags() & 0x07) << 13) | ((0 & 0x1fff) << 0);
		packetDataWriter.writeShort(flagsFragmentOffset);
		packetDataWriter.writeByte(packet.getTimeToLive());
		packetDataWriter.writeByte(packet.getProtocol());
		packetDataWriter.writeShort(0); // checksum
		packetDataWriter.write(packet.getSource().getAddress());
		packetDataWriter.write(packet.getDestination().getAddress());
		
		int headerSize = packetDataWriter.size();
		int ihl = headerSize >> 2;
		
		ProtocolDataUnitFormatter payloadFormatter = getPayloadFormatter(packet
				.getProtocol());
		payloadFormatter.format(packet.getPayload(), packetDataWriter);
		
		byte[] bytes = packetDataWriter.getData();
		int totalLength = bytes.length;
		
		bytes[0] |= ihl & 0x0f;
		bytes[2] = (byte) (totalLength >> 8);
		bytes[3] = (byte) (totalLength >> 0);
		
		short checksum = InternetChecksum.calculateChecksum(bytes, 0,
				headerSize);
		bytes[10] = (byte) (checksum >> 8);
		bytes[11] = (byte) (checksum >> 0);
		
		dataWriter.write(bytes);
	}
	public IPv4Packet parse(DataReader dataReader) {
		IPv4Packet packet = new IPv4Packet();
		
		byte versionIHL = dataReader.readByte();
		packet.setVersion((byte) ((versionIHL >> 4) & 0x0f));
		int ihl = (versionIHL & 0x0f);
		packet.setTypeOfService(dataReader.readByte());
		dataReader.readUnsignedShort(); // total length
		packet.setIdentication(dataReader.readUnsignedShort());
		short flagsFragmentOffset = dataReader.readShort();
		packet.setFlags((byte) ((flagsFragmentOffset >> 13) & 0x07));
		int fragmentOffset = flagsFragmentOffset & 0x1fff;
		if (fragmentOffset != 0)
			throw new UnsupportedOperationException(
					"Unsupported fragment offset");
		packet.setTimeToLive((short) dataReader.readUnsignedByte());
		packet.setProtocol(dataReader.readByte());
		dataReader.readShort(); // checksum
		packet.setSource(dataReader.readIP4Addres());
		packet.setDestination(dataReader.readIP4Addres());
		
		for (int i = 5; i < ihl; i++) {
			dataReader.readInt(); // kill options
		}
		
		ProtocolDataUnitFormatter payloadFormatter = getPayloadFormatter(packet
				.getProtocol());
		packet.setPayload(payloadFormatter.parse(dataReader));
		
		return packet;
	}
	
}