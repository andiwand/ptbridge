package at.stefl.ptbridge.ptmp;

import java.math.BigInteger;
import java.security.MessageDigest;


public enum PTMPAuthentication {
	
	CLEAR_TEXT() {
		public String calculateDigest(String password, String challenge) {
			return password;
		}
	},
	SIMPLE() {
		public String calculateDigest(String password, String challenge) {
			String result = "";
			for (int i = 0; i < password.length(); i++)
				result += (char) (158 - password.charAt(i));
			return result;
		}
	},
	MD5() {
		public static final String PASSWORD_CHARSET = "UTF-8";
		
		public String calculateDigest(String password, String challenge) {
			try {
				byte[] bytes = password.getBytes(PASSWORD_CHARSET);
				
				MessageDigest messageDigest = MessageDigest.getInstance("MD5");
				byte[] digestBytes = messageDigest.digest(bytes);
				
				BigInteger digest = new BigInteger(1, digestBytes);
				return String.format("%1$032X", digest);
			} catch (Exception e) {}
			
			return null;
		}
	};
	
	public abstract String calculateDigest(String password, String challenge);
	
}