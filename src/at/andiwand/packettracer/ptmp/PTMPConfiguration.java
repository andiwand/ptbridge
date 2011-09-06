package at.andiwand.packettracer.ptmp;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class PTMPConfiguration {
	
	public static final int ENCODING_TEXT	= 1;
	public static final int ENCODING_BINARY	= 2;
	public static final Set<Integer> ENCODING_SET = Collections
			.unmodifiableSet(new HashSet<Integer>(Arrays.asList(ENCODING_TEXT,
					ENCODING_BINARY)));
	
	public static final int ENCRYPTION_NONE	= 1;
	public static final int ENCRYPTION_XOR	= 2;
	public static final Set<Integer> ENCRYPTION_SET = Collections
			.unmodifiableSet(new HashSet<Integer>(Arrays.asList(
					ENCRYPTION_NONE, ENCRYPTION_XOR)));
	
	public static final int COMPRESSION_NO				= 1;
	public static final int COMPRESSION_ZLIB_DEFAULT	= 2;
	public static final Set<Integer> COMPRESSION_SET = Collections
			.unmodifiableSet(new HashSet<Integer>(Arrays.asList(COMPRESSION_NO,
					COMPRESSION_ZLIB_DEFAULT)));
	
	public static final int AUTHENTICATION_CLEAR_TEXT	= 1;
	public static final int AUTHENTICATION_SIMPLE		= 2;
	public static final int AUTHENTICATION_MD5			= 4;
	public static final Set<Integer> AUTHENTICATION_SET = Collections
			.unmodifiableSet(new HashSet<Integer>(Arrays.asList(
					AUTHENTICATION_CLEAR_TEXT, AUTHENTICATION_SIMPLE,
					AUTHENTICATION_MD5)));
	
	
	public static final int DEFAULT_ENCODING		= ENCODING_TEXT;
	public static final int DEFAULT_ENCRYPTION		= ENCRYPTION_NONE;
	public static final int DEFAULT_COMPRESSION		= COMPRESSION_NO;
	public static final int DEFAULT_AUTHENTICATION	= AUTHENTICATION_CLEAR_TEXT;
	
	public static final PTMPConfiguration DEFAULT = new PTMPConfiguration(
			DEFAULT_ENCODING, DEFAULT_ENCRYPTION, DEFAULT_COMPRESSION,
			DEFAULT_AUTHENTICATION);
	
	
	
	public static boolean legalEncoding(int encoding) {
		return ENCODING_SET.contains(encoding);
	}
	public static boolean legalEncryption(int encryption) {
		return ENCRYPTION_SET.contains(encryption);
	}
	public static boolean legalCompression(int compression) {
		return COMPRESSION_SET.contains(compression);
	}
	public static boolean legalAuthentication(int authentication) {
		return AUTHENTICATION_SET.contains(authentication);
	}
	
	public static PTMPAuthenticationMethod getAuthenticationMethod(
			int authentication) {
		switch (authentication) {
		case AUTHENTICATION_CLEAR_TEXT:
			return PTMPAuthenticationMethod.CLEAR_TEXT;
		case AUTHENTICATION_SIMPLE:
			return PTMPAuthenticationMethod.SIMPLE;
		case AUTHENTICATION_MD5:
			return PTMPAuthenticationMethod.MD5;
		
		default:
			throw new RuntimeException("Unknown authentication: "
					+ authentication);
		}
	}
	public static PTMPAuthenticationMethod getAuthenticationMethod(
			PTMPConfiguration configuration) {
		return getAuthenticationMethod(configuration.authentication);
	}
	
	
	
	
	private final int encoding;
	private final int encryption;
	private final int compression;
	private final int authentication;
	
	
	public PTMPConfiguration(int encoding, int encryption, int compression,
			int authentication) {
		if (!legalEncoding(encoding))
			throw new RuntimeException("Unknown encoding: " + encoding);
		if (!legalEncryption(encryption))
			throw new RuntimeException("Unknown encryption: " + encryption);
		if (!legalCompression(compression))
			throw new RuntimeException("Unknown compression: " + compression);
		if (!legalAuthentication(authentication))
			throw new RuntimeException("Unknown authentication: "
					+ authentication);
		
		this.encoding = encoding;
		this.encryption = encryption;
		this.compression = compression;
		this.authentication = authentication;
	}
	
	
	public String toString() {
		return "encoding: " + encoding + "; " +
			"encryption: " + encryption + "; " +
			"compression: " + compression + "; " +
			"authentication: " + authentication + ";";
	}
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof PTMPConfiguration)) return false;
		PTMPConfiguration configuration = (PTMPConfiguration) obj;
		
		return (encoding == configuration.encoding) &&
			(encryption == configuration.encryption) &&
			(compression == configuration.compression) &&
			(authentication == configuration.authentication);
	}
	public int hashCode() {
		return Arrays.hashCode(new int[] {encoding, encryption, compression,
				authentication});
	}
	
	public int encoding() {
		return encoding;
	}
	public int encryption() {
		return encryption;
	}
	public int compression() {
		return compression;
	}
	public int authentication() {
		return authentication;
	}
	public PTMPAuthenticationMethod getAuthenticationMethod() {
		return getAuthenticationMethod(authentication);
	}
	
}