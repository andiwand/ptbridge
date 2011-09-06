package at.andiwand.packettracer.traverser.translator;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.network.Assignments;
import at.andiwand.library.network.mac.MACAddress;
import at.andiwand.library.network.pdu.DHCPPacket;
import at.andiwand.library.network.pdu.ProtocolDataUnit;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserDHCPPacket;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserProtocolDataUnit;


public class DHCPTranslator extends ProtocolDataUnitTranslator {
	
	public static final Inet4Address EMPTY_ADDRESS;
	
	private static final Map<Byte, Byte> OPERATION_ASSOCIATION_MAP = new HashMap<Byte, Byte>();
	
	
	static {
		try {
			EMPTY_ADDRESS = (Inet4Address) Inet4Address.getByName("0.0.0.0");
		} catch (UnknownHostException e) {
			throw new IllegalStateException("Unreachable section");
		}
		
		OPERATION_ASSOCIATION_MAP.put(Assignments.DHCP.MESSAGE_TYPE_DISCOVER,
				Assignments.DHCP.OPERATION_BOOT_REQUEST);
		OPERATION_ASSOCIATION_MAP.put(Assignments.DHCP.MESSAGE_TYPE_OFFER,
				Assignments.DHCP.OPERATION_BOOT_REPLY);
		OPERATION_ASSOCIATION_MAP.put(Assignments.DHCP.MESSAGE_TYPE_REQUEST,
				Assignments.DHCP.OPERATION_BOOT_REQUEST);
		OPERATION_ASSOCIATION_MAP.put(Assignments.DHCP.MESSAGE_TYPE_DECLINE,
				Assignments.DHCP.OPERATION_BOOT_REQUEST);
		OPERATION_ASSOCIATION_MAP.put(Assignments.DHCP.MESSAGE_TYPE_ACK,
				Assignments.DHCP.OPERATION_BOOT_REPLY);
		OPERATION_ASSOCIATION_MAP.put(Assignments.DHCP.MESSAGE_TYPE_NAK,
				Assignments.DHCP.OPERATION_BOOT_REPLY);
		OPERATION_ASSOCIATION_MAP.put(Assignments.DHCP.MESSAGE_TYPE_RELEASE,
				Assignments.DHCP.OPERATION_BOOT_REQUEST);
		OPERATION_ASSOCIATION_MAP.put(Assignments.DHCP.MESSAGE_TYPE_INFORM,
				Assignments.DHCP.OPERATION_BOOT_REQUEST);
	}
	
	
	
	public MultiuserDHCPPacket translate(ProtocolDataUnit protocolDataUnit) {
		if (!(protocolDataUnit instanceof DHCPPacket))
			throw new IllegalArgumentException("Illegal PDU class");
		DHCPPacket packet = (DHCPPacket) protocolDataUnit;
		
		byte messageType = 0;
		String transactionId = Integer.toHexString(packet.getTransactionId());
		Inet4Address gatewayAddress = EMPTY_ADDRESS;
		Inet4Address subnetMask = EMPTY_ADDRESS;
		Inet4Address tftpServerAddress = EMPTY_ADDRESS;
		Inet4Address dnsServerAddress = EMPTY_ADDRESS;
		
		for (DHCPPacket.Option option : packet.getOptions()) {
			if (option instanceof DHCPPacket.Option.MessageType) {
				messageType = ((DHCPPacket.Option.MessageType) option)
						.getMessageType();
			} else if (option instanceof DHCPPacket.Option.Router) {
				gatewayAddress = ((DHCPPacket.Option.Router) option)
						.getAddresses().get(0);
			} else if (option instanceof DHCPPacket.Option.SubnetMask) {
				subnetMask = ((DHCPPacket.Option.SubnetMask) option)
						.getAddress();
			} else if (option instanceof DHCPPacket.Option.NameServer) {
				dnsServerAddress = ((DHCPPacket.Option.NameServer) option)
						.getAddresses().get(0);
			}
		}
		
		MultiuserDHCPPacket result = new MultiuserDHCPPacket();
		
		result.setMessageType(messageType);
		result.setUnknown1((byte) 0);
		result.setUnknown2((byte) 0);
		result.setUnknown3((byte) 0);
		result.setTransactionId(transactionId);
		result.setSecondsElapsed((short) packet.getSecondsElapsed());
		result.setFlags(packet.getFlags());
		result.setUnknown4(0);
		result.setUnknown5(0);
		result.setUnknown6(0);
		result.setClientAddress(packet.getClientAddess());
		result.setYourAddress(packet.getYourAddess());
		result.setServerAddress(packet.getServerAddess());
		result.setGatewayAddress(gatewayAddress);
		result.setSubnetMask(subnetMask);
		result.setClientHardwareAddress((MACAddress) packet
				.getClientHardwareAddress());
		result.setServerName(packet.getServerName());
		result.setFile(packet.getFile());
		result.setTftpServerAddress(tftpServerAddress);
		result.setUnknown7(0);
		result.setUnknown8("");
		result.setDnsServerAddress(dnsServerAddress);
		
		return result;
	}
	
	public DHCPPacket translate(MultiuserProtocolDataUnit protocolDataUnit) {
		if (!(protocolDataUnit instanceof MultiuserDHCPPacket))
			throw new IllegalArgumentException("Illegal PDU class");
		MultiuserDHCPPacket packet = (MultiuserDHCPPacket) protocolDataUnit;
		
		byte operation = OPERATION_ASSOCIATION_MAP.get(packet.getMessageType());
		int transactionId = Integer.parseInt(packet.getTransactionId(), 16);
		
		DHCPPacket result = new DHCPPacket();
		
		result.setOperation(operation);
		result.setHardwareType(Assignments.ARP.HARDWARE_TYPE_ETHERNET);
		result.setHops((short) 0);
		result.setTransactionId(transactionId);
		result.setSecondsElapsed(packet.getSecondsElapsed());
		result.setFlags(packet.getFlags());
		result.setClientAddess(packet.getClientAddress());
		result.setYourAddess(packet.getYourAddress());
		result.setServerAddess(packet.getServerAddress());
		result.setRelayAgentAddess(EMPTY_ADDRESS);
		result.setClientHardwareAddress(packet.getClientHardwareAddress());
		result.setServerName(packet.getServerName());
		result.setFile(packet.getFile());
		
		DHCPPacket.Option.MessageType messageTypeOption = new DHCPPacket.Option.MessageType();
		messageTypeOption.setMessageType(packet.getMessageType());
		result.addOption(messageTypeOption);
		
		DHCPPacket.Option.SubnetMask subnetMaskOption = new DHCPPacket.Option.SubnetMask();
		subnetMaskOption.setAddress(packet.getSubnetMask());
		result.addOption(subnetMaskOption);
		
		DHCPPacket.Option.Router routerOption = new DHCPPacket.Option.Router();
		routerOption.addAddress(packet.getGatewayAddress());
		result.addOption(routerOption);
		
		DHCPPacket.Option.NameServer nameServerOption = new DHCPPacket.Option.NameServer();
		nameServerOption.addAddress(packet.getDnsServerAddress());
		result.addOption(nameServerOption);
		
		return result;
	}
	
}