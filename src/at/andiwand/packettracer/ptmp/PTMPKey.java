package at.andiwand.packettracer.ptmp;

import java.io.UnsupportedEncodingException;
import java.util.UUID;


public class PTMPKey {
	
	public static final int KEY_SIZE = 101;
	public static final String KEY_CHARSET = "US-ASCII";
	
	
	public static byte[] createKey(UUID serverUuid, UUID clientUuid,
			PTMPTimestamp serverTimestamp, PTMPTimestamp clientTimestamp) {
		String key = "";
		
		key += "{" + serverTimestamp + "}";
		key += "{" + clientUuid + "}";
		key += "PTMP";
		key += serverTimestamp;
		key += clientTimestamp;
		
		try {
			return key.getBytes(KEY_CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	
	
	private PTMPKey() {}
	
}