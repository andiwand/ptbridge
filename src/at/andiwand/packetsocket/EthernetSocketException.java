package at.andiwand.packetsocket;

import java.io.IOException;


public class EthernetSocketException extends IOException {
	
	private static final long serialVersionUID = 5581335357595821236L;
	
	public EthernetSocketException() {}
	public EthernetSocketException(String message) {
		super(message);
	}
	
}