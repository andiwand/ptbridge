package at.andiwand.library.network.pdu;

import java.net.Inet4Address;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class DHCPPacket extends ProtocolDataUnit {
	
	public static abstract class Option extends ProtocolDataUnit {
		public static abstract class IPTemplate extends Option {
			private Inet4Address address;
			
			public Inet4Address getAddress() {
				return address;
			}
			public void setAddress(Inet4Address address) {
				this.address = address;
			}
		}
		public static abstract class IPListTemplate extends Option {
			private List<Inet4Address> addresses = new LinkedList<Inet4Address>();
			
			
			public List<Inet4Address> getAddresses() {
				return Collections.unmodifiableList(addresses);
			}
			public int size() {
				return addresses.size();
			}
			public boolean isEmpty() {
				return addresses.isEmpty();
			}
			
			public void setAddresses(List<Inet4Address> addresses) {
				this.addresses = new LinkedList<Inet4Address>(addresses);
			}
			public void addAddress(Inet4Address address) {
				addresses.add(address);
			}
			public void removeAddress(Inet4Address address) {
				addresses.remove(address);
			}
		}
		
		public static class SubnetMask extends IPTemplate {
			
		}
		public static class Router extends IPListTemplate {
			
		}
		public static class NameServer extends IPListTemplate {
			
		}
		public static class RequestedIPAddress extends IPTemplate {
			
		}
		public static class MessageType extends Option {
			private byte type;
			
			public byte getMessageType() {
				return type;
			}
			public void setMessageType(byte type) {
				this.type = type;
			}
		}
		public static class ServerIdentifier extends IPTemplate {
			
		}
		
		
		
		private byte type;
		
		public final byte getType() {
			return type;
		}
		public final void setType(byte type) {
			this.type = type;
		}
	}
	
	
	
	private byte operation;
	private short hardwareType;
	private short hops;
	private int transactionId;
	private int secondsElapsed;
	private short flags;
	private Inet4Address clientAddess;
	private Inet4Address yourAddess;
	private Inet4Address serverAddess;
	private Inet4Address relayAgentAddess;
	private Object clientHardwareAddress;
	private String serverName;
	private String file;
	private List<Option> options = new LinkedList<Option>();
	
	
	public byte getOperation() {
		return operation;
	}
	public short getHardwareType() {
		return hardwareType;
	}
	public short getHops() {
		return hops;
	}
	public int getTransactionId() {
		return transactionId;
	}
	public int getSecondsElapsed() {
		return secondsElapsed;
	}
	public short getFlags() {
		return flags;
	}
	public Inet4Address getClientAddess() {
		return clientAddess;
	}
	public Inet4Address getYourAddess() {
		return yourAddess;
	}
	public Inet4Address getServerAddess() {
		return serverAddess;
	}
	public Inet4Address getRelayAgentAddess() {
		return relayAgentAddess;
	}
	public Object getClientHardwareAddress() {
		return clientHardwareAddress;
	}
	public String getServerName() {
		return serverName;
	}
	public String getFile() {
		return file;
	}
	public List<Option> getOptions() {
		return Collections.unmodifiableList(options);
	}
	public Option getOption(byte type) {
		for (Option option : options) {
			if (option.getType() == type)
				return option;
		}
		
		return null;
	}
	
	public void setOperation(byte operation) {
		this.operation = operation;
	}
	public void setHardwareType(short hardwareType) {
		this.hardwareType = hardwareType;
	}
	public void setHops(short hops) {
		this.hops = hops;
	}
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	public void setSecondsElapsed(int secondsElapsed) {
		this.secondsElapsed = secondsElapsed;
	}
	public void setFlags(short flags) {
		this.flags = flags;
	}
	public void setClientAddess(Inet4Address clientAddess) {
		this.clientAddess = clientAddess;
	}
	public void setYourAddess(Inet4Address yourAddess) {
		this.yourAddess = yourAddess;
	}
	public void setServerAddess(Inet4Address serverAddess) {
		this.serverAddess = serverAddess;
	}
	public void setRelayAgentAddess(Inet4Address relayAgentAddess) {
		this.relayAgentAddess = relayAgentAddess;
	}
	public void setClientHardwareAddress(Object clientHardwareAddress) {
		this.clientHardwareAddress = clientHardwareAddress;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public void setOptions(List<Option> options) {
		this.options = new LinkedList<Option>(options);
	}
	public void addOption(Option option) {
		options.add(option);
	}
	public void removeOption(Option option) {
		options.remove(option);
	}
	
}