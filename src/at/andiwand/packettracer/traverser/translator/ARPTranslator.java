package at.andiwand.packettracer.traverser.translator;

import java.net.Inet4Address;

import at.andiwand.library.network.mac.MACAddress;
import at.andiwand.library.network.pdu.ARPPacket;
import at.andiwand.library.network.pdu.ProtocolDataUnit;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserARPPacket;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserProtocolDataUnit;


public class ARPTranslator extends ProtocolDataUnitTranslator {
	
	public MultiuserARPPacket translate(ProtocolDataUnit protocolDataUnit) {
		if (!(protocolDataUnit instanceof ARPPacket))
			throw new IllegalArgumentException("Illegal PDU class");
		ARPPacket packet = (ARPPacket) protocolDataUnit;
		
		MultiuserARPPacket result = new MultiuserARPPacket();
		
		result.setHardwareType(packet.getHardwareType());
		result.setProtocolType(packet.getOperation());
		result.setOperation(packet.getOperation());
		result.setSenderHardwareAddress((MACAddress) packet
				.getSenderHardwareAddress());
		result.setTargetHardwareAddress((MACAddress) packet
				.getTargetHardwareAddress());
		result.setSenderProtocolAddress((Inet4Address) packet
				.getSenderProtocolAddress());
		result.setTargetProtocolAddress((Inet4Address) packet
				.getTargetProtocolAddress());
		
		return result;
	}
	
	public ARPPacket translate(MultiuserProtocolDataUnit protocolDataUnit) {
		if (!(protocolDataUnit instanceof MultiuserARPPacket))
			throw new IllegalArgumentException("Illegal PDU class");
		MultiuserARPPacket packet = (MultiuserARPPacket) protocolDataUnit;
		
		ARPPacket result = new ARPPacket();
		
		result.setHardwareType(packet.getHardwareType());
		result.setProtocolType(packet.getOperation());
		result.setOperation(packet.getOperation());
		result.setSenderHardwareAddress(packet.getSenderHardwareAddress());
		result.setSenderProtocolAddress(packet.getSenderProtocolAddress());
		result.setTargetHardwareAddress(packet.getTargetHardwareAddress());
		result.setTargetProtocolAddress(packet.getTargetProtocolAddress());
		
		return result;
	}
	
}