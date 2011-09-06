package at.andiwand.packettracer.ptmp;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public enum PTMPAuthenticationMethod {
	
	CLEAR_TEXT() {
		public String calculateDigest(String password) {
			return password;
		}
	},
	SIMPLE() {
		public String calculateDigest(String password) {
			String result = "";
			
			for (int i = 0; i < password.length(); i++) {
				result += (char) (158 - password.charAt(i));
			}
			
			return result;
		}
	},
	MD5() {
		public static final String PASSWORD_CHARSET = "UTF-8";
		
		public String calculateDigest(String password) {
			try {
				byte[] bytes = password.getBytes(PASSWORD_CHARSET);
				
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				byte[] digestBytes = md5.digest(bytes);
				BigInteger digest = new BigInteger(1, digestBytes);
				
				return String.format("%1$032X", digest);
			} catch (UnsupportedEncodingException e) {
			} catch (NoSuchAlgorithmException e) {}
			
			return null;
		}
	};
	
	public abstract String calculateDigest(String password);
	
}