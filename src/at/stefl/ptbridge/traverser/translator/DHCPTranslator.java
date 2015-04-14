package at.stefl.ptbridge.traverser.translator;

import java.util.HashMap;
import java.util.Map;

import at.stefl.commons.network.Assignments;
import at.stefl.commons.network.ip.IPv4Address;
import at.stefl.commons.network.ip.SubnetMask;
import at.stefl.commons.network.mac.MACAddress;
import at.stefl.packetsocket.pdu.DHCPPacket;
import at.stefl.ptbridge.ptmp.multiuser.pdu.MultiuserDHCPPacket;


public class DHCPTranslator extends
		GenericPDUTranslator<DHCPPacket, MultiuserDHCPPacket> {
	
	private static final Map<Byte, Byte> OPERATION_ASSOCIATION_MAP = new HashMap<Byte, Byte>();
	
	static {
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
	
	@Override
	protected MultiuserDHCPPacket toMultiuserGeneric(DHCPPacket packet) {
		byte messageType = 0;
		String transactionId = Integer.toHexString(packet.getTransactionId());
		IPv4Address gatewayAddress = IPv4Address.EMPTY;
		SubnetMask subnetMask = new SubnetMask(0);
		IPv4Address tftpServerAddress = IPv4Address.EMPTY;
		IPv4Address dnsServerAddress = IPv4Address.EMPTY;
		
		for (DHCPPacket.Option option : packet.getOptions()) {
			if (option instanceof DHCPPacket.MessageTypeOption) {
				messageType = ((DHCPPacket.MessageTypeOption) option)
						.getMessageType();
			} else if (option instanceof DHCPPacket.RouterOption) {
				gatewayAddress = ((DHCPPacket.RouterOption) option)
						.getAddresses().get(0);
			} else if (option instanceof DHCPPacket.SubnetMaskOption) {
				subnetMask = ((DHCPPacket.SubnetMaskOption) option)
						.getSubnetMask();
			} else if (option instanceof DHCPPacket.NameServerOption) {
				dnsServerAddress = ((DHCPPacket.NameServerOption) option)
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
		result.setTFTPServerAddress(tftpServerAddress);
		result.setUnknown7(0);
		result.setUnknown8("");
		result.setDNSServerAddress(dnsServerAddress);
		
		return result;
	}
	
	@Override
	protected DHCPPacket toNetworkGeneric(MultiuserDHCPPacket packet) {
		byte operation = OPERATION_ASSOCIATION_MAP.get(packet.getMessageType());
		int transactionId = Integer.parseInt(packet.getTransactionId(), 16);
		
		DHCPPacket result = new DHCPPacket();
		
		result.setOperation(operation);
		result.setHardwareType(Assignments.ARP.HARDWARE_TYPE_ETHERNET);
		result.setHops((short) 0);
		result.setTransactionId(transactionId);
		result.setSecondsElapsed(packet.getSecondsElapsed());
		result.setFlags(packet.getFlags());
		result.setClientAddress(packet.getClientAddress());
		result.setYourAddress(packet.getYourAddress());
		result.setServerAddress(packet.getServerAddress());
		result.setRelayAgentAddress(IPv4Address.EMPTY);
		result.setClientHardwareAddress(packet.getClientHardwareAddress());
		result.setServerName(packet.getServerName());
		result.setFile(packet.getFile());
		
		DHCPPacket.MessageTypeOption messageTypeOption = new DHCPPacket.MessageTypeOption();
		messageTypeOption.setMessageType(packet.getMessageType());
		result.addOption(messageTypeOption);
		
		DHCPPacket.SubnetMaskOption subnetMaskOption = new DHCPPacket.SubnetMaskOption();
		subnetMaskOption.setSubnetMask(packet.getSubnetMask());
		result.addOption(subnetMaskOption);
		
		DHCPPacket.RouterOption routerOption = new DHCPPacket.RouterOption();
		routerOption.addAddress(packet.getGatewayAddress());
		result.addOption(routerOption);
		
		DHCPPacket.NameServerOption nameServerOption = new DHCPPacket.NameServerOption();
		nameServerOption.addAddress(packet.getDNSServerAddress());
		result.addOption(nameServerOption);
		
		return result;
	}
	
}