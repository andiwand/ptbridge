package at.andiwand.library.network.pdu.formatter;

import java.net.Inet4Address;
import java.nio.charset.Charset;
import java.util.Arrays;

import at.andiwand.library.network.Assignments;
import at.andiwand.library.network.pdu.DHCPPacket;
import at.andiwand.library.network.pdu.ProtocolDataUnit;
import at.andiwand.library.network.pdu.DHCPPacket.Option;
import at.andiwand.library.util.DataReader;
import at.andiwand.library.util.DataWriter;


//TODO change address length system
//TODO change address read/write system
public class DHCPPacketFormatter extends ProtocolDataUnitFormatter {
	
	public static final Charset CHARSET = Charset.forName("ascii");
	
	public static final int CLIENT_HARDWARE_ADDRESS_SIZE = 16;
	public static final int SERVER_NAME_SIZE = 64;
	public static final int FILE_SIZE = 128;
	
	
	public static void writeHardwareAddress(Object address, short hardwareType,
			DataWriter dataWriter) {
		DataWriter addressDataWriter = new DataWriter();
		ARPPacketFormatter.writeHardwareAddress(address, hardwareType,
				addressDataWriter);
		
		dataWriter.write(addressDataWriter.getData(), 0,
				CLIENT_HARDWARE_ADDRESS_SIZE);
	}
	public static Object readHardwareAddress(short hardwareType,
			DataReader dataReader) {
		byte[] addressBuffer = new byte[CLIENT_HARDWARE_ADDRESS_SIZE];
		dataReader.read(addressBuffer);
		
		DataReader addressDataReader = new DataReader(addressBuffer);
		return ARPPacketFormatter.readHardwareAddress(hardwareType,
				addressDataReader);
	}
	
	
	
	public void format(ProtocolDataUnit protocolDataUnit, DataWriter dataWriter) {
		if (!(protocolDataUnit instanceof DHCPPacket))
			throw new IllegalArgumentException("Illegal PDU class");
		DHCPPacket packet = (DHCPPacket) protocolDataUnit;
		
		dataWriter.writeByte(packet.getOperation());
		dataWriter.writeByte(packet.getHardwareType());
		dataWriter.writeByte(Assignments.ARP.getHardwareLength(packet
				.getHardwareType()));
		dataWriter.writeByte(packet.getHops());
		dataWriter.writeInt(packet.getTransactionId());
		dataWriter.writeShort(packet.getSecondsElapsed());
		dataWriter.writeShort(packet.getFlags());
		dataWriter.write(packet.getClientAddess().getAddress());
		dataWriter.write(packet.getYourAddess().getAddress());
		dataWriter.write(packet.getServerAddess().getAddress());
		dataWriter.write(packet.getRelayAgentAddess().getAddress());
		writeHardwareAddress(packet.getClientHardwareAddress(), packet
				.getHardwareType(), dataWriter);
		dataWriter.write(Arrays.copyOf(
				packet.getServerName().getBytes(CHARSET), SERVER_NAME_SIZE));
		dataWriter.write(Arrays.copyOf(
				packet.getServerName().getBytes(CHARSET), FILE_SIZE));
		dataWriter.writeInt(Assignments.DHCP.MAGIC_COOKIE_DHCP);
		
		for (Option option : packet.getOptions()) {
			dataWriter.writeByte(option.getType());
			
			switch (option.getType()) {
			case Assignments.DHCP.OPTION_SUBNET_MASK:
			case Assignments.DHCP.OPTION_REQUESTED_IP_ADDRESS:
			case Assignments.DHCP.OPTION_SERVER_IDENTIFIER:
				Option.IPTemplate ip = (Option.IPTemplate) option;
				
				dataWriter.writeByte(Assignments.ARP.IPV4_ADDRESS_LENGTH);
				dataWriter.write(ip.getAddress().getAddress());
				break;
			case Assignments.DHCP.OPTION_ROUTER:
			case Assignments.DHCP.OPTION_NAME_SERVER:
				Option.IPListTemplate ipList = (Option.IPListTemplate) option;
				if (ipList.isEmpty())
					throw new IllegalArgumentException("IP list is empty");
				
				dataWriter.writeByte(Assignments.ARP.IPV4_ADDRESS_LENGTH
						* ipList.size());
				for (Inet4Address address : ipList.getAddresses()) {
					dataWriter.write(address.getAddress());
				}
				break;
			case Assignments.DHCP.OPTION_MESSAGE_TYPE:
				Option.MessageType messageType = (Option.MessageType) option;
				
				dataWriter.writeByte(1);
				dataWriter.writeByte(messageType.getType());
				break;
			
			default:
				throw new IllegalStateException("Unreachable section");
			}
		}
		
		dataWriter.writeByte(Assignments.DHCP.OPTION_END);
	}
	
	public DHCPPacket parse(DataReader dataReader) {
		DHCPPacket packet = new DHCPPacket();
		
		byte[] stringBuffer = new byte[FILE_SIZE];
		
		packet.setOperation(dataReader.readByte());
		packet.setHardwareType(dataReader.readByte());
		if (dataReader.readByte() != Assignments.ARP.getHardwareLength(packet
				.getHardwareType()))
			throw new IllegalStateException("Illegal hardware address length");
		packet.setHops((short) dataReader.readUnsignedByte());
		packet.setTransactionId(dataReader.readInt());
		packet.setSecondsElapsed(dataReader.readUnsignedShort());
		packet.setFlags(dataReader.readShort());
		packet.setClientAddess(dataReader.readIP4Addres());
		packet.setYourAddess(dataReader.readIP4Addres());
		packet.setServerAddess(dataReader.readIP4Addres());
		packet.setRelayAgentAddess(dataReader.readIP4Addres());
		packet.setClientHardwareAddress(readHardwareAddress(packet
				.getHardwareType(), dataReader));
		dataReader.read(stringBuffer, 0, SERVER_NAME_SIZE);
		packet.setServerName(new String(stringBuffer, 0, SERVER_NAME_SIZE,
				CHARSET).trim());
		dataReader.read(stringBuffer);
		packet.setFile(new String(stringBuffer, CHARSET).trim());
		dataReader.readInt(); // magic cookie
		
		while (true) {
			byte optionType = dataReader.readByte();
			
			if (optionType == Assignments.DHCP.OPTION_PAD)
				continue;
			if (optionType == Assignments.DHCP.OPTION_END)
				break;
			
			int optionLength = dataReader.readUnsignedByte();
			
			byte[] optionBuffer = new byte[optionLength];
			dataReader.read(optionBuffer);
			DataReader optionDataReader = new DataReader(optionBuffer);
			
			switch (optionType) {
			case Assignments.DHCP.OPTION_SUBNET_MASK:
				Option.IPTemplate ip;
				ip = new Option.SubnetMask();
			case Assignments.DHCP.OPTION_REQUESTED_IP_ADDRESS:
				ip = new Option.RequestedIPAddress();
			case Assignments.DHCP.OPTION_SERVER_IDENTIFIER:
				ip = new Option.SubnetMask();
				ip.setAddress(dataReader.readIP4Addres());
				
				packet.addOption(ip);
				break;
			case Assignments.DHCP.OPTION_ROUTER:
				Option.IPListTemplate ipList;
				ipList = new Option.Router();
			case Assignments.DHCP.OPTION_NAME_SERVER:
				ipList = new Option.NameServer();
				
				for (int i = 0; i < optionLength; i += Assignments.ARP.IPV4_ADDRESS_LENGTH) {
					ipList.addAddress(dataReader.readIP4Addres());
				}
				
				packet.addOption(ipList);
				break;
			case Assignments.DHCP.OPTION_MESSAGE_TYPE:
				Option.MessageType messageType = new Option.MessageType();
				messageType.setType(optionDataReader.readByte());
				
				packet.addOption(messageType);
				break;
			
			default:
//				throw new IllegalStateException("Unreachable section");
				new IllegalStateException("Unreachable section")
						.printStackTrace();
			}
		}
		
		return packet;
	}
	
}