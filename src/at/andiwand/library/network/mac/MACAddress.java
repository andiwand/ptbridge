package at.andiwand.library.network.mac;

import java.util.Arrays;
import java.util.Random;


public class MACAddress {
	
	public static final int SIZE = 6;
	public static final String SEPARATOR = ":";
	public static final int RADIX = 16;
	
	public static final MACAddress BROADCAST_ADDRESS = getByAddress("ff:ff:ff:ff:ff:ff");
	
	
	public static MACAddress getByAddress(String address) {
		return getByAddress(address.split(SEPARATOR));
	}
	public static MACAddress getByAddress(String... address) {
		if (address.length != SIZE)
			throw new IllegalArgumentException("The address has a illegal length!");
		
		MACAddress result = new MACAddress();
		for (int i = 0; i < SIZE; i++)
			result.address[i] = (byte) Integer.parseInt(address[i], RADIX);
		return result;
	}
	public static MACAddress getByAddress(byte[] address, int offset) {
		if ((address.length - offset) < SIZE)
			throw new IllegalArgumentException("The address has a illegal length!");
		
		MACAddress result = new MACAddress();
		System.arraycopy(address, offset, result.address, 0, SIZE);
		return result;
	}
	public static MACAddress getByAddress(byte... address) {
		return getByAddress(address, 0);
	}
	public static MACAddress getByAddress(int... address) {
		if (address.length != SIZE)
			throw new IllegalArgumentException("The address has a illegal length!");
		
		MACAddress result = new MACAddress();
		for (int i = 0; i < SIZE; i++)
			result.address[i] = (byte) address[i];
		return result;
	}
	public static MACAddress getByAddress(long address) {
		MACAddress result = new MACAddress();
		for (int i = 0; i < SIZE; i++)
			result.address[i] = (byte) (address >> ((SIZE - 1 - i) << 3));
		return result;
	}
	
	public static MACAddress randomAddress() {
		MACAddress result = new MACAddress();
		Random random = new Random();
		
		for (int i = 1; i < SIZE; i++) {
			result.address[i] = (byte) random.nextInt();
		}
		
		return result;
	}
	
	
	
	private final byte[] address = new byte[SIZE];
	
	
	private MACAddress() {}
	
	
	public int hashCode() {
		return Arrays.hashCode(address);
	}
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof MACAddress)) return false;
		MACAddress address = (MACAddress) obj;
		
		return Arrays.equals(this.address, address.address);
	}
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < SIZE; i++) {
			String tmp = Integer.toHexString(address[i] & 0xff);
			if (tmp.length() < 2) tmp = "0" + tmp;
			builder.append(tmp);
			builder.append(SEPARATOR);
		}
		
		return builder.substring(0, builder.length() - SEPARATOR.length());
	}
	
	public byte[] getAddress() {
		byte[] result = new byte[SIZE];
		System.arraycopy(address, 0, result, 0, SIZE);
		
		return result;
	}
	public boolean isBroadcast() {
		return equals(BROADCAST_ADDRESS);
	}
	
	public long toLong() {
		long result = 0;
		
		for (int i = 0; i < SIZE; i++)
			result |= (long) (address[i] & 0xff) << ((SIZE - 1 - i) << 3);
		
		return result;
	}
	
}