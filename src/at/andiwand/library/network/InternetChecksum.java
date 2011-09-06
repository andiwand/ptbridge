package at.andiwand.library.network;


public class InternetChecksum {
	
	public static short calculateChecksum(byte[] buffer) {
		return calculateChecksum(buffer, 0, buffer.length);
	}
	public static short calculateChecksum(byte[] buffer, int length) {
		return calculateChecksum(buffer, 0, length);
	}
	public static short calculateChecksum(byte[] buffer, int offset, int length) {
		if ((length & 1) != 0)
			throw new IllegalArgumentException("Illegal length");
		
		int result = 0;
		
		for (int i = offset; i < offset + length; ) {
			result += ((buffer[i++] & 0xff) << 8) | ((buffer[i++] & 0xff) << 0);
		}
		
		result = ((result >> 0) & 0xffff) + ((result >> 16) & 0xffff);
		
		return (short) ~result;
	}
	
	private InternetChecksum() {}
	
}