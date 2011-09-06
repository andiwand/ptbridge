package at.andiwand.library.network.pdu.formatter;

import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.network.Assignments;
import at.andiwand.library.network.pdu.Ethernet2Frame;
import at.andiwand.library.network.pdu.ProtocolDataUnit;
import at.andiwand.library.util.DataReader;
import at.andiwand.library.util.DataWriter;


public class Ethernet2FrameFormatter extends ProtocolDataUnitFormatter {
	
	private static final Map<Short, ProtocolDataUnitFormatter> DEFAULT_PAYLOAD_FORMATTERS = new HashMap<Short, ProtocolDataUnitFormatter>();
	
	static {
		DEFAULT_PAYLOAD_FORMATTERS.put(Assignments.Ethernet.TYPE_IPV4,
				new IPv4PacketFormatter());
		DEFAULT_PAYLOAD_FORMATTERS.put(Assignments.Ethernet.TYPE_ARP,
				new ARPPacketFormatter());
		DEFAULT_PAYLOAD_FORMATTERS.put(Assignments.Ethernet.TYPE_IPV6,
				new IPv6PacketFormatter());
	}
	
	
	
	private final Map<Short, ProtocolDataUnitFormatter> payloadFormatters;
	
	
	public Ethernet2FrameFormatter() {
		this(true);
	}
	public Ethernet2FrameFormatter(boolean defaultPayloadFormatters) {
		if (defaultPayloadFormatters) {
			payloadFormatters = new HashMap<Short, ProtocolDataUnitFormatter>(
					DEFAULT_PAYLOAD_FORMATTERS);
		} else {
			payloadFormatters = new HashMap<Short, ProtocolDataUnitFormatter>();
		}
	}
	
	
	private ProtocolDataUnitFormatter getPayloadFormatter(short type) {
		ProtocolDataUnitFormatter result = payloadFormatters.get(type);
		
		if (result == null)
			throw new IllegalArgumentException("Unregistered type");
		
		return result;
	}
	
	public void format(ProtocolDataUnit protocolDataUnit, DataWriter dataWriter) {
		if (!(protocolDataUnit instanceof Ethernet2Frame))
			throw new IllegalArgumentException("Illegal PDU class");
		Ethernet2Frame frame = (Ethernet2Frame) protocolDataUnit;
		
		dataWriter.writeMACAddress(frame.getDestination());
		dataWriter.writeMACAddress(frame.getSource());
		dataWriter.writeShort(frame.getType());
		
		ProtocolDataUnitFormatter payloadFormatter = getPayloadFormatter(frame
				.getType());
		payloadFormatter.format(frame.getPayload(), dataWriter);
	}
	public Ethernet2Frame parse(DataReader dataReader) {
		Ethernet2Frame frame = new Ethernet2Frame();
		
		frame.setDestination(dataReader.readMACAddress());
		frame.setSource(dataReader.readMACAddress());
		frame.setType(dataReader.readShort());
		
		ProtocolDataUnitFormatter payloadFormatter = getPayloadFormatter(frame
				.getType());
		frame.setPayload(payloadFormatter.parse(dataReader));
		
		return frame;
	}
	
}